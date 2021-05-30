export const sqlInsertMsg = "INSERT INTO messages (Id, ConversationId, Message, MessageType, SenderId) VALUES (?, ?, ?, ?, ?);";

export const sqlUpdateParticipants = "UPDATE participants SET MessageTitle = ?, ModifiedDate = ? WHERE ConversationId = ?;";

export const sqlGetRoomByUserId = "SELECT p.ConversationId FROM participants p WHERE p.UserId = ? ORDER BY p.ModifiedDate DESC LIMIT ?;"

export const procGetListFriendIds = "CALL Proc_GetListFriend(?,?);"

export const sqlGetFriendAvtivites = "SELECT a.Id, a.Status, a.ModifiedDate FROM activities a WHERE a.Id IN(?);";

export const sqlGetFriendSocketByUserIds = "SELECT SocketId FROM user_socket WHERE UserId IN(?);"
