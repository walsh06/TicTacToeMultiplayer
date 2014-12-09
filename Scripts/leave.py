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


cur = db.cursor()
#get the game from the database
cur.execute("""SELECT * 
			FROM games
			WHERE id = %s""", fs["game_id"].value)

result = cur.fetchone()

#if player 2 is already empty
#player 1 is leaving and delete the game from the table
#else one of the players is leaving
if result[2] == 0:
	cur.execute("""DELETE FROM games
				WHERE id = %s""", fs["game_id"].value)
else : #Gameover + rematch stuff

	#if winner is 0
	#then the game is still being played
	if result[5] == 0:
		#update the records of each user in the game
		if result[1] == int(fs["player_id"].value):
			cur.execute("""UPDATE users 
						SET games_played = games_played + 1, games_lost = games_lost +1
						WHERE id = %s""", result[1])

			cur.execute("""UPDATE users 
						SET games_played = games_played + 1, games_won = games_won +1
						WHERE id = %s""", result[2])
		else:
			cur.execute("""UPDATE users 
						SET games_played = games_played + 1, games_lost = games_lost +1
						WHERE id = %s""", result[2])

			cur.execute("""UPDATE users 
						SET games_played = games_played + 1, games_won = games_won +1
						WHERE id = %s""", result[1])

	#if player 1 is leaving 
	#remove them and make player 2 player 1
	#else just remove player 2
	if result[1] == int(fs["player_id"].value):
		userID =str(result[2])
		cur.execute("""UPDATE games 
					SET player_1 = %s, player_2 = 0
					WHERE id = %s""", (userID, fs["game_id"].value))
		
	else:
		cur.execute("""UPDATE games
				SET player_2 = 0
				WHERE id = %s""", (fs["game_id"].value))

	#reset the game
	cur.execute("""UPDATE games 
						SET current_player = player_1,  board_state = "BBBBBBBBB", winner = 0
						WHERE id = %s""", fs["game_id"].value)


print json.dumps({"success" : True})


db.commit()