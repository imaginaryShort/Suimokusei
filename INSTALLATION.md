# ローカル開発環境

# Server
## Requirements
Node.js v0.12.7
MySQL 5.5

## Install
```
/server $npm install
```

## Deploy
サーバにSSHして
```
$ git pull
$ cd KB_03/server
$ forever start -c npm\ start app.js
```

# Android
## Requirements
Android Studio 1.3
Android 5.0 Lollipop

# Device
## Requirements
Raspberry pi 2
Raspbian
Python 2.7.3

# DataBase
Table: householdid
HouseholdId Integer
HouseName   String

Table: User
Id    Integer
Name  String
Status      ENUM('still','walk','run','bicycle','sleep','meal')
Updated     TIMESTAMP

# API
Userテーブルの全データを出力
GET http://imaginaryshort.com:7000/status
特定のHouseholdIdに属するUserのデータを出力
GET http://imaginaryshort.com:7000/status?hid={value}
POST http://imaginaryshort.com:7000/status?mynumber={value}&status={value}

GET http://imaginaryshort.com:7000/householdid?={value}
POST http://imaginarysohrt.com:7000/householdid?={value}
