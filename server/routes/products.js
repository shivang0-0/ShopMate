const express = require('express');
const router = express.Router();
const Product = require('../models/Product');
const Category = require('../models/Category');

router.get('/', async (req, res) => {
    const { category, product_id } = req.query; 

    try {
        if (category) {
            const products = await Product.find({ category: category });
            return res.json(products);
        }

        if (product_id) {
            const product = await Product.findOne({ id: product_id });
            if (product) {
                return res.json(product);
            } else {
                return res.status(404).json({ message: 'Product not found' });
            }
        }

        const products = await Product.find();
        res.json(products);

    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});


module.exports = router;
