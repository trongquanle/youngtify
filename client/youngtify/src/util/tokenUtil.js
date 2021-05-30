import jwt_decode from 'jwt-decode';
import moment from 'moment';
import jwt from 'jsonwebtoken';

/**
 * Decode token
 * Author: LTQUAN (13/02/2021)
 */
export const tokenDecode = () => {
    let token = localStorage.getItem('accessToken');
    if (token) {
        return jwt_decode(token);
    }
    return null;
}

/**
 * Check token còn hạn hay k
 * Author: LTQUAN (13/02/2021)
 */
export const tokenExpried = () => {
    const token = tokenDecode();
    let exp;
    if (token)
        exp = token.exp;
    if (exp && moment.unix(exp) > moment()) {
        return true;
    }
    return false;
}

export const token = () => {
    return localStorage.getItem('accessToken');
}

export const getAccessToken = () => {
    var now = Math.floor(Date.now() / 1000);
    var exp = now + 3600;

    var header = { cty: "stringee-api;v=1" };
    var payload = {
        jti: process.env.VUE_APP_STRINGEE_API_KEY + "-" + now,
        iss: process.env.VUE_APP_STRINGEE_API_KEY,
        exp: exp,
        rest_api: true
    };
    var token = jwt.sign(payload, process.env.VUE_APP_STRINGEE_SECRET, { algorithm: 'HS256', header: header })
    return token;
}