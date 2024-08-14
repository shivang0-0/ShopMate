const mongoose = require('mongoose');

const StoreSchema = new mongoose.Schema({
    store_name: { type: String, required: true },
    location: { type: String, required: true },
    products: [
        {
            product: { type: mongoose.Schema.Types.ObjectId, ref: 'Product' },
            quantity: { type: Number, required: true }
        }
    ],
});

module.exports = mongoose.model('Store', StoreSchema);