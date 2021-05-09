import "reflect-metadata";
import { createConnection } from "typeorm";
import * as bodyParser from "body-parser";
import * as cors from 'cors';
import { Request, Response, NextFunction } from "express";
import { Routes } from "./routes";
// import {User} from "./entity/User";
import { App } from './app';
import * as multer from 'multer';

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

createConnection().then(async connection => {

    // create express app
    const app = new App().getApp();
    app.use(bodyParser.json());
    app.use(cors());
    app.use(require('express').static('public'));

    // register express routes from defined application routes
    Routes.forEach(route => {
        (app as any)[route.method](route.route, (req: Request, res: Response, next: NextFunction) => {
            const result = (new (route.controller as any))[route.action](req, res, next);
            if (result instanceof Promise) {
                result.then(result => result !== null && result !== undefined ? res.send(result) : undefined);
            } else if (result !== null && result !== undefined) {
                res.json(result);
            }
        });
    });

    const uploadAvatar = multer({ storage: storageAvatars });
    const uploadImage = multer({ storage: storageImages });

    app.post("/api/storage", uploadAvatar.single("file"), (req: any, res) => {
        try {
            let {file} = req;
            let filename: string;
            if (!file) filename = null;
            else filename = `/avatars/${file.filename}`;
            return res.status(200).json({ fileName: filename });
        } catch (error) {
            return res.status(200).json({ fileName: null });
        }
    });

    app.post("/api/storage/multiple", uploadImage.array("files", 12), (req: any, res) => {
        try {
            let {files} = req;
            let filenames = []
            if (files && files.length > 0){
                filenames = files.map(file => `/images/${file.filename}`);
            }
            return res.status(200).json({ fileNames: filenames });
        } catch (error) {
            return res.status(200).json({ fileNames: null });
        }
    })

}).catch(error => console.log(error));
