import jwt_decode from 'jwt-decode';
import moment from 'moment';

/**
 * Decode token
 * Author: LTQUAN (13/02/2021)
 */
export const tokenDecode = () => {
    let token = localStorage.getItem('accessToken');
    if(token){
        return jwt_decode(token);
    }
    return null;
}

/**
 * Check token còn hạn hay k
 * Author: LTQUAN (13/02/2021)
 */
export const tokenExpried = () => {
    let exp = tokenDecode()?.exp;
    if(exp && moment.unix(exp) > moment()){
        return true;
    }
    return false;
}

export const token = () => {
    return localStorage.getItem('accessToken');
}