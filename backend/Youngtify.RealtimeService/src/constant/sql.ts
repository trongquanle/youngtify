export const sqlInsertMsg = "INSERT INTO messages (Id, ConversationId, Message, MessageType, SenderId) VALUES (?, ?, ?, ?, ?);";

export const sqlUpdateParticipants = "UPDATE participants SET MessageTitle = ?, ModifiedDate = ? WHERE ConversationId = ?;";

export const sqlGetRoomByUserId = "SELECT p.ConversationId FROM participants p WHERE p.UserId = ? ORDER BY p.ModifiedDate DESC LIMIT ?;"