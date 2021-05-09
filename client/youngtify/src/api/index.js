import axios from 'axios';

const apiInstance = axios.create({
    baseURL: 'http://localhost:9090'
    // timeout: 10000
});

/**
 * Hàm chung gọi api sang phía MonitorBankend
 * url: /api/v1/transactions, configRequest: { method: "GET", params: { pageIndex: 0, pageSize: 10 } }
 * url: /api/v1/transactions, configRequest: { method: "POST", data: { code: "VTB",... } }
 * @param {String} url 
 * @param {AxiosRequestConfig} configRequest bao gồm method, params(GET), data(POST,PUT), headers...
 * Author: LTQUAN (11/12/2020)
 */
export const request = (url, configRequest) => {
    return new Promise((reslove, reject) => {
        apiInstance(url, configRequest)
            .then(res => reslove(res))
            .catch(err => reject(err));
    });
}