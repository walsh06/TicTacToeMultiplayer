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
			WHERE id = %s""", fs["id"].value)

select_game = cur.fetchone()
#if the game was retrieved
#create the game object and return it
if select_game != None:
	game = {'id' : select_game[0],
			'player_1' : select_game[1],
			'player_2' : select_game[2],
			'current_player': select_game[3],
			'board_state': select_game[4]}
	print json.dumps(game)
else :
	print json.dumps({"id": -1})
    