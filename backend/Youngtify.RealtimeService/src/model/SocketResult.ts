export class SocketResult {

    constructor(isSuccess: boolean, data: object){
        this.isSuccess = isSuccess;
        this.data = data;
    }

    isSuccess: boolean;
    data: object;
}