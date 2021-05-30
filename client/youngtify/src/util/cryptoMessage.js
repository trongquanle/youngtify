import {AES, MD5, enc} from 'crypto-js';

export const getKey = (keyGen) => {
    return MD5(`${keyGen}-${process.env.VUE_APP_SECRET_KEY}`).toString();
}

export const encryptMessage = (plantText, key) => {
    try {
        return AES.encrypt(plantText, key).toString();
    } catch (error) {
        return undefined;
    }
}

export const decryptMessage = (ciphertext, key) => {
    try {
        return AES.decrypt(ciphertext, key).toString(enc.Utf8);
    } catch (error) {
        return undefined;
    }
}

