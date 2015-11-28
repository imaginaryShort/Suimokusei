# ローカル開発環境

# Server
## Requirements
Node.js v0.12.7
MySQL 5.5

## Install
```
cd KB_03/server
$npm install
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
* Android Studio 1.3
* Android 5.0 Lollipop

# Device
## Requirements
* Raspberry pi 2
* Raspbian
* Python 2.7.3

# DataBase
```
Table: household
Hid         Integer
HouseName   String
```

```
Table: User
Id          Integer
Name        String
Hid         Integer
Status      ENUM('still','walk','run','bicycle','sleep','meal')
Updated     TIMESTAMP
```

# API
Userテーブルの全データを出力
GET http://imaginaryshort.com:7000/

特定のHouseholdIdに属するUserのデータを出力
GET http://imaginaryshort.com:7000/hid?hid={value}

特定のユーザの状態をアップデートする
POST http://imaginaryshort.com:7000/?mynumber={value}&status={value}

# Add sample data
http://imaginaryshort.com:7000/add?id=1&name=test
