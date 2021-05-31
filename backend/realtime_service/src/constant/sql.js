const sqlInsertMsg = "INSERT INTO messages (Id, ConversationId, Message, MessageType, SenderId) VALUES (?, ?, ?, ?, ?);";

const sqlUpdateParticipants = "UPDATE participants SET MessageTitle = ?, ModifiedDate = ?, MessageType = ?, SenderId = ? WHERE ConversationId = ?;";

const sqlGetRoomByUserId = "SELECT p.ConversationId FROM participants p WHERE p.UserId = ? ORDER BY p.ModifiedDate DESC LIMIT ?;"

const procGetListFriendIds = "CALL Proc_GetListFriend(?,?);"

const sqlGetFriendAvtivites = "SELECT a.Id, a.Status, a.ModifiedDate FROM activities a WHERE a.Id IN(?);";

const sqlGetFriendSocketByUserIds = "SELECT SocketId FROM user_socket WHERE UserId IN(?);"

const sqlUpdateActivity = "UPDATE activities SET Status = ?, ModifiedDate = ? WHERE Id = ?;";

const sqlInsertUserSocket = "INSERT user_socket(UserId, SocketId) VALUES(?,?);";

const sqlDeleteUserSocket = "DELETE FROM user_socket WHERE SocketId = ?;"

const sqlTruncateUserSocket = "TRUNCATE user_socket;"

const sqlInsertNotification = "INSERT notifications(PostId,UserId,Content,ModifiedDate) VALUES(?,?,?,?);"

const sqlGetNotificationNotSeen = "SELECT COUNT(*) AS Count FROM notifications WHERE UserId = ? AND IsSeen = 0;";

const sqlGetSocketIdByUserId = "SELECT SocketId FROM user_socket WHERE UserId = ?;";

const sqlGetNotifications = `
SELECT n.Id AS id, n.Content AS content, n.IsSeen AS isSeen, n.CreatedDate as createdDate, TRIM(CONCAT(p.LastName, ' ', p.FirstName)) AS fullName, a.AvatarUrl AS avatarUrl
FROM notifications n JOIN profiles p ON n.PostId = p.Id LEFT JOIN avatars a ON n.PostId = a.UserId
WHERE (a.IsUsing OR a.IsUsing IS NULL) AND n.UserId = ?
ORDER BY n.Id DESC;
`

const sqlInsertVideocall = "INSERT INTO user_videocall (SenderId, UserId, SenderSocketId, RoomId) VALUES (?, ?, ?, ?);"

const sqlUpdateVideocall = "UPDATE user_videocall SET UserSocketId = ? WHERE RoomId = ?;";

const sqlGetVideoCallByRoomId = "SELECT * FROM user_videocall WHERE RoomId = ?;";

const sqlGetVideoCallBySocketId = "SELECT * FROM user_videocall WHERE SenderSocketId = ? OR UserSocketId = ?;";

const sqlDeleteVideoCallById = "DELETE FROM user_videocall WHERE Id = ?";

const sqlUpdateNotification = "UPDATE notifications SET IsSeen = 1 WHERE UserId = ? AND IsSeen = 0;"

module.exports = {
    sqlInsertMsg,
    sqlUpdateParticipants,
    sqlGetRoomByUserId,
    procGetListFriendIds,
    sqlGetFriendAvtivites,
    sqlGetFriendSocketByUserIds,
    sqlUpdateActivity,
    sqlInsertUserSocket,
    sqlDeleteUserSocket,
    sqlTruncateUserSocket,
    sqlInsertNotification,
    sqlGetNotificationNotSeen,
    sqlGetSocketIdByUserId,
    sqlGetNotifications,
    sqlInsertVideocall,
    sqlUpdateVideocall,
    sqlGetVideoCallByRoomId,
    sqlGetVideoCallBySocketId,
    sqlDeleteVideoCallById,
    sqlUpdateNotification
}