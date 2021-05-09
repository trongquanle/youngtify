const express = require('express');
const app = express();
const server = require('http').createServer(app);

app.use(express.static('public'));

const io = require('socket.io')(server, {
    cors: {
      origin: "*",
      credentials: true
    }
  });

io.sockets.on('connect', (socket) => {
    console.log(socket.id);
    socket.on('signCode', code => {
      socket.code = code;
      io.in(socket.id).emit('receivedCode', code);
    });
    socket.on('sendMsg', msg => {
        console.log(socket.code);
        io.emit('receivedMsg', {code: socket.code, msg});
    });
});

server.listen(process.env.PORT || 3000, () => {
    console.log('Server is running...');
})