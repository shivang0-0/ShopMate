const mongoose = require('mongoose');

const RatingSchema = new mongoose.Schema({
    rate: Number,
    count: Number,
});

const ProductSchema = new mongoose.Schema({
    id: { type: Number, unique: true, required: true }, 
    title: { type: String, required: true },
    price: { type: Number, required: true },
    description: { type: String, required: true },
    category: { type: String, required: true },
    image: { type: String, required: true },
    rating: RatingSchema,
});

module.exports = mongoose.model('Product', ProductSchema);