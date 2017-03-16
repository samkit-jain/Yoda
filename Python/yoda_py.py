import os
import socket
import sys

HOST = ''
PORT = 8080

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

try:
	s.bind((HOST, PORT))
except socket.error, msg:
	print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
	sys.exit()

s.listen(10)

while True:
    conn, addr = s.accept()
    print "Connected by: ", addr
    data = conn.recv(1024) #how many bytes of data will the server receive
    print "Received: ", data
    
    if data[0:4] == "open":
    	command = "gnome-terminal -e '" + data[5:] + "'"
    	os.system(command)
    elif data.lower() == "turn on wifi" or data.lower() == "turn on wi-fi":
    	command = "nmcli nm wifi on"
    	os.system(command)
    elif data.lower() == "turn off wifi" or data.lower() == "turn off wi-fi":
    	command = "nmcli nm wifi off"
    	os.system(command)
    elif data.lower() == "shutdown" or data.lower() == "shut down":
    	command = "poweroff"
    	os.system(command)
    elif data.lower() == "restart" or data.lower() == "reboot":
    	command = "reboot"
    	os.system(command)

    conn.close()

s.close()