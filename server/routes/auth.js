const express = require('express');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const User = require('../models/User');

const router = express.Router();

// @route   POST /register
// @desc    Register a user
// @access  Public
router.post('/register', async (req, res) => {
  const { email, password, firstname, lastname, mobile } = req.body;

  try {
    if (!email || !password || !firstname || !lastname || !mobile) {
      var fieldMissing = [];
      if (!email) fieldMissing.push('email');
      if (!password) fieldMissing.push('password');
      if (!firstname) fieldMissing.push('firstname');
      if (!lastname) fieldMissing.push('lastname');
      if (!mobile) fieldMissing.push('mobile');
      return res.status(400).json({ msg: 'Please enter all required fields' , fieldMissing: fieldMissing });
    }

    let user = await User.findOne({ email });

    if (user) {
      return res.status(400).json({ msg: 'User already exists' });
    }

    user = new User({
      email,
      password,
      firstname,
      lastname,
      mobile
    });

    const salt = await bcrypt.genSalt(10);
    user.password = await bcrypt.hash(password, salt);

    await user.save();

    const payload = {
      user: {
        id: user.id,
      },
    };

    jwt.sign(
      payload,
      process.env.JWT_SECRET,
      { expiresIn: '24h' },
      (err, token) => {
        if (err) throw err;
        res.json({ token , success: true, message: 'User registered successfully' });
      }
    );
  } catch (err) {
    console.error(err.message);
    res.status(500).send('Server error');
  }
});

// @route   POST /login
// @desc    Authenticate user and get token
// @access  Public
router.post('/login', async (req, res) => {
  const { email, password } = req.body;

  try {
    let user = await User.findOne({ email });

    if (!user) {
      return res.status(400).json({ msg: 'Invalid credentials' });
    }

    const isMatch = await bcrypt.compare(password, user.password);

    if (!isMatch) {
      return res.status(400).json({ msg: 'Invalid credentials' });
    }

    const payload = {
      user: {
        id: user.id,
      },
    };

    jwt.sign(
      payload,
      process.env.JWT_SECRET,
      { expiresIn: '24h' },
      (err, token) => {
        if (err) throw err;
        res.json({ token, success: true, message: 'User logged in successfully' });
      }
    );
  } catch (err) {
    console.error(err.message);
    res.status(500).send('Server error');
  }
});

module.exports = router;
