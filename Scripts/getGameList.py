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
#get all games with no player2
cur.execute("""SELECT id
			FROM games
			WHERE player_2 = 0""")

result = cur.fetchall()

if result != None:
	#add each id to a list and return the list
	game_ids = []
	for game_id in result:
		game_ids.append(game_id[0])
	#print game_ids
	print json.dumps({"success": True, "game_id": game_ids})
else :
	game_ids =result[0]
	print json.dumps({"success": False, "game_id": game_ids})

cur.close()
    
