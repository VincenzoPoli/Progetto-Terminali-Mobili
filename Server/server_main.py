import pyautogui
import socket
import _thread
import json
import time
from pynput.keyboard import Key, Controller

def decodificaMsg(keyboard, msg, debugLevel):
    esito = True
    try:
        if msg == "MCL":                        #mouse click sinistra
            pyautogui.click()
        elif msg == "MCR":                      #mouse click destra
            pyautogui.click(button='right')
        elif ',' in msg:               #es: MD20 -> mouse sotto 20 pixel
            msg = msg.replace("MX", "")
            msg = msg.replace("MY", "")
            coordinates = [int(x) for x in msg.split(',')]
            coox, cooy = pyautogui.position()
            coox += coordinates[0]
            cooy += coordinates[1]
            pyautogui.moveTo(coox, cooy, 0.2)
            if debugLevel > 1: print("sposto il mouse di ", msg, " pixel")
        elif msg.startswith("MP_"):  #MP_X_Y Posizione diretta del cursore
            msg = msg.replace("MP_", "")
            list_of_integers = [int(x) for x in msg.split('_')] 
            pyautogui.moveTo(list_of_integers[0], list_of_integers[1])
        elif msg == "CHR_ENT":          #vai a capo
            keyboard.press(Key.enter)
            keyboard.release(Key.enter)
        elif msg == "CHR_BKSP":        #backspaces
            keyboard.press(Key.backspace)
            keyboard.release(Key.backspace)
        elif msg.startswith("CHR_"):    #scrivi testo
            msg = msg.replace("CHR_", "")
            msg = msg.replace("\n", "")
            for c in msg:
                keyboard.type(c)
        else:
            esito = False
    except Exception as e:
        if debugLevel > 0: print("Errore durante la decodifica di: ", msg)
        if debugLevel > 0: print("Exception:", str(e))
        esito = False

    return esito

#Quando un Dispositivo si autentica
def onNewClient(clientsocket, keyboard):
    exit = False
    debugLevel = 0
    
    while not exit:
        msg = clientsocket.recv(32768)
        strEsito = "OK"

        if not msg:
            exit = True
        else:
            try:
                msg = msg.decode("utf-8")
                if debugLevel > 1: print(msg)
                
                if decodificaMsg(keyboard, msg, debugLevel):
                    strEsito = "OK"
                else:
                    strEsito = "NF"
            except Exception as e:
                if debugLevel > 0: print("Errore durante l'elaborazione di: ", msg)
                if debugLevel > 0: print("Exception:", str(e))
                strEsito = "EX"

        clientsocket.send(strEsito.encode())

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) # START a socket subject s

f = open("ip.txt", "r")
computerIP = f.read()
f.close
port = 9194
keyboard = Controller()
clients = []
s.bind((computerIP, port))
print(computerIP)
s.listen(8)
print("Socket in ascolto su porta ", port)
pyautogui.FAILSAFE = False

while True:
    #Accetto Connessioni Client
    (c, addr) = s.accept()     
    print("Richiesta di connessione da ", addr)
    _thread.start_new_thread(onNewClient, (c, keyboard))

