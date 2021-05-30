const multer = require("multer");

const storageAvatars = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, './public/avatars');
    },
    filename: (req, file, cb) => {
        cb(null, Date.now() + '_' + file.originalname);
    }
});
const storageImages = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, './public/images');
    },
    filename: (req, file, cb) => {
        cb(null, Date.now() + '_' + file.originalname);
    }
});

const uploadAvatar = multer({ storage: storageAvatars });
const uploadImage = multer({ storage: storageImages });

module.exports = {
    uploadAvatar,
    uploadImage
}