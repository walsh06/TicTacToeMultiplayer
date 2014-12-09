#!C:/Python27/python.exe

import MySQLdb
import json
import cgi
#get url fields
fs = cgi.FieldStorage()

print "Content-Type: text/html; charset=utf-8\n\n";
#connect to database
db = MySQLdb.connect(host = "localhost",
					 user = "cathal",
					 passwd = "password",
					 db = "tictactoe_db")

cur = db.cursor() 
#get the top 10 players sorted by win percentage
cur.execute("""SELECT username, games_played, games_won, games_lost, games_tied
			FROM users
			ORDER BY games_won / games_played DESC 
			LIMIT 0 , 10""")

result = cur.fetchall()

leaderboard = []
#add titles to the leaderboard
leaderboard.append("Name, GP, W, D, L")
#for each result add them to the leaderboard array
for x in result:
	leaderboard.append(str(x[0]) + "," + str(x[1]) +"," + str(x[2]) + "," +str(x[4])  +"," + str(x[3]))

print json.dumps({"success": True, "leaderboard" : leaderboard})