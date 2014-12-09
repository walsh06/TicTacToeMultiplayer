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
#get the game from database
cur.execute("""SELECT *
			FROM games
			WHERE id = %s""", fs["id"].value)

select_game = cur.fetchone()

#if winner is not 0 
#the game is over
#else the game is still running
if select_game[5] != 0:
	game = {'change' : 2,
			'id' : select_game[0],
			'player_1' : select_game[1],
			'player_2' : select_game[2],
			'current_player': select_game[3],
			'board_state': select_game[4],
			'winner': select_game[5],
			'rematch': select_game[6]}

else:
	board_string = select_game[4]

	#if the board passed in is the same as the database
	#there has been no change in the game
	#else return the game
	if board_string == fs["board_state"].value:
		game = {'change' : 0,
				'id' : select_game[0],
				'player_1' : select_game[1],
				'player_2' : select_game[2],
				'current_player': select_game[3],
				'board_state': select_game[4]}
	else:
		game = {'change' : 1,
				'id' : select_game[0],
				'player_1' : select_game[1],
				'player_2' : select_game[2],
				'current_player': select_game[3],
				'board_state': select_game[4]}

print json.dumps(game)
		
cur.close()