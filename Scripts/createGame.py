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
#get the games that player is in
cur.execute("""SELECT id
			FROM games
			WHERE player_1 = %s""", fs["id"].value)

result = cur.fetchone()
#if they are not in a game
#create a new game
#return the id of their new game
if result == None:
	cur.execute("""INSERT INTO games(player_1, current_player) 
			VALUES (%s, %s)""", (fs["id"].value, fs["id"].value))

	cur.execute("""SELECT id
			FROM games
			WHERE player_1 = %s""", fs["id"].value)

	game_id = cur.fetchone()[0]
	db.commit()
	print json.dumps({"success": True, "game_id": game_id})
else :
	game_id =result[0]
	print json.dumps({"success": False, "game_id": game_id})

cur.close()
    
