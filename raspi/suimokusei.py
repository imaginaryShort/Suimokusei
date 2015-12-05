import json
import websocket

def on_message(ws, message):
    print message

def on_error(ws, error):
    print error

def on_close(ws):
    print "### closed ###"

def on_open(ws):
    ws.send("{\"UUID\": 1}")
    #ws.close()



if __name__ == "__main__":
    f = open("env.json")
    env = json.load(f)
    f.close()

    #websocket.enableTrace(True)
    ws = websocket.WebSocketApp(env["server"],
                                on_message = on_message,
                                on_error = on_error,
                                on_close = on_close)
    ws.on_open = on_open
    ws.run_forever()
