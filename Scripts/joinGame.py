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

#get the game and check if it has a player 2 already
cur.execute("""SELECT count(id)
			FROM games
			WHERE player_2 = 0 AND id = %s""", (fs["gameid"].value))
			
player_2 = cur.fetchone()

#if no player 2 add the player to the game
#return the game id
if player_2[0] == 1 :
	cur.execute("""UPDATE GAMES SET player_2 = %s WHERE id = %s """ 
			%(fs["playerid"].value, fs["gameid"].value))
	db.commit()
	print json.dumps({"success": True, "game_id" : fs["gameid"].value})
else:
	print json.dumps({"success": False})

cur.close()
    
