#!C:/Python27/python.exe

import MySQLdb
import json
import cgi
fs = cgi.FieldStorage() #get the url fields

print "Content-Type: text/html; charset=utf-8\n\n";
#connect to database
db = MySQLdb.connect(host = "localhost",
					 user = "cathal",
					 passwd = "password",
					 db = "tictactoe_db")

cur = db.cursor()
#get the game
cur.execute("""SELECT * 
		FROM games
		WHERE id = %s""", fs["gameid"].value)


result = cur.fetchone()

#if 0 then no one has requested rematch yet
#set rematch to their id
#else if its not 0 and not their id
#reset the game
if result[6] == 0:
	cur.execute("""UPDATE games SET rematch = %s WHERE id = %s """ 
		%(fs["playerid"].value, fs["gameid"].value))
elif result[6] != int(fs["playerid"].value):
	#reset game
	cur.execute("""UPDATE games SET rematch = 0, winner = 0, board_state = 'BBBBBBBBB'
	 WHERE id = %s """, fs["gameid"].value)

#commit and return result
db.commit()
print json.dumps({"success": True, "message": "Game Updated"})
cur.close()