#!C:/Python27/python.exe

import MySQLdb
import json
import cgi
#get url fields
fs = cgi.FieldStorage()

print "Content-Type: text/html; charset=utf-8\n\n";
#connect to the database
db = MySQLdb.connect(host = "localhost",
					 user = "cathal",
					 passwd = "password",
					 db = "tictactoe_db")

i = 0
board = fs["board"].value
win = False
#check the board for possible wins
while i < 9 :
	if board[i] == board[i+1] and board[i] == board[i+2] and board[i] != 'B':
		win = True
	i = i + 3

for i in range(0,3):
	if board[i] == board[i+3] and board[i] == board[i+6] and board[i] != 'B':
		win = True
		#do stuff

if board[0] == board[4] == board[8] != 'B':
	win = True

if board[2] == board[4] == board[6] != 'B':
	win = True


cur = db.cursor() 
#if there is no B on the board and the game is not over
#the game is a draw
#else if the game is a win
#else update the game
if "B" not in fs["board"].value and win == False:
	#update the game in the database
	cur.execute("""UPDATE games SET current_player = %s ,board_state= '%s', winner = -1 WHERE id = %s """ 
			%(fs["playerid"].value, fs["board"].value, fs["gameid"].value))

	cur.execute("""SELECT *
			FROM games
			WHERE id = %s""", fs["gameid"].value)

	result = cur.fetchone()
	#update the user records
	cur.execute("""UPDATE users
				SET games_played = games_played + 1, games_tied = games_tied + 1
				WHERE id = %s OR id = %s""", (result[1], result[2]))
			
elif win == True:
	
	cur.execute("""SELECT * 
			FROM games
			WHERE id = %s""", fs["gameid"].value)

	result = cur.fetchone()
	userID =str(result[3])
	#update the game in the database
	cur.execute("""UPDATE games SET current_player = %s ,board_state= '%s', winner = %s WHERE id = %s """ 
		%(fs["playerid"].value, fs["board"].value, userID, fs["gameid"].value))

	cur.execute("""SELECT * 
			FROM games
			WHERE id = %s""", fs["gameid"].value)

	result = cur.fetchone()
	
	#update the user records in the database
	cur.execute("""UPDATE users SET games_played = games_played + 1, games_won = games_won +1
					WHERE id = %s""", result[5])

	cur.execute("""UPDATE users SET games_played = games_played + 1, games_lost = games_lost +1
					WHERE id = %s""", result[3])


else :
	#update the game
	cur.execute("""UPDATE games SET current_player = %s ,board_state= '%s' WHERE id = %s """ 
		%(fs["playerid"].value, fs["board"].value, fs["gameid"].value))

db.commit()
print json.dumps({"success": True, "message": "Game Updated"})