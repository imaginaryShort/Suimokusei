#include <ESP8266WiFi.h>

const char* ssid     = "CLfCmRv6C5VNzMEF";
const char* password = "AliiTlshns8wARNR";

const char* host = "imaginaryshort.com";
const int httpPort = 7000;

boolean isOpen = false;

// io 16 lead switch
// io 4 red 
// io 5 blue

int leadPin = 16;
int red = 4;
void sendRequest(char* status);


void setup() {
  Serial.begin(115200);
  delay(10);

  // We start by connecting to a WiFi network

  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");  
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  pinMode(leadPin, INPUT);
  pinMode(red, OUTPUT);
}


void loop() {
  delay(1000);
 
  if (digitalRead(leadPin) == LOW){
    Serial.println("LOW\n");
    digitalWrite(red, HIGH);
    if(isOpen == false){
      sendRequest("close");
      isOpen = true;
    }
  }else{
    Serial.println("HIGH\n");
    digitalWrite(red, LOW);
    if(isOpen == true){
      sendRequest("open");
      isOpen = false;
    }
  }
}

void sendRequest(char* status){
  Serial.print("connecting to ");
  Serial.println(host);

  // Use WiFiClient class to create TCP connections
  WiFiClient client;
  if (!client.connect(host, httpPort)) {
    Serial.println("connection failed");
    return;
  }
  
  // We now create a URI for the request
  String url = "/status/add?seid=1&status=";
  url += status;
  
  Serial.print("Requesting URL: ");
  Serial.println(url);
  
  // This will send the request to the server
  client.print(String("GET ") + url + " HTTP/1.1\r\n" +
               "Host: " + host + "\r\n" + 
               "Connection: close\r\n\r\n");
  delay(10);

  // Read all the lines of the reply from server and print them to Serial
  while(client.available()){
    String line = client.readStringUntil('\r');
    Serial.print(line);
  }
  
  Serial.println();
  Serial.println("closing connection");
}

