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
<table>
<tr><th>Status</th><td>Type</td><td>Details</td></tr>
<tr><td>UserId</td><td>INTEGER</td><td>Primary key</td></tr>
<tr><td>Status</td><td>ENUM('', 'still', 'walk', 'run', 'bicycle', 'sleep', 'meal', 'refrigerator')</td><td></td></tr>
<tr><td>Updated</td><td>TIMESTAMP</td><td></td></tr>
</table>

<table>
<tr><th>User</th><td>Type</td><td>Details</td></tr>
<tr><td>Id</td><td>INTEGER</td><td>Auto increment, Primary key</td></tr>
<tr><td>Name</td><td>TEXT</td><td></td></tr>
<tr><td>HomeId</td><td>INTEGER</td><td></td></tr>
<tr><td>Order</td><td>INTEGER</td><td></td></tr>
<tr><td>Updated</td><td>TIMESTAMP</td><td></td></tr>
</table>

<table>
<tr><th>Home</th><td>Type</td><td>Details</td></tr>
<tr><td>Id</td><td>INTEGER</td><td>Auto increment, Primary key</td></tr>
<tr><td>Name</td><td>TEXT</td><td></td></tr>
</table>

# API
* Userテーブルの全データを出力
  - GET http://imaginaryshort.com:7000/

* 特定のHomeIdに属するUserのデータを出力
  - GET http://imaginaryshort.com:7000/hid?hid={value}

* 特定のユーザのStatusをアップデートする
  - GET http://imaginaryshort.com:7000/add?id={value}&status={value}&homeid={value}

* Homeテーブルの情報を出力
  - GET http://imaginaryshort.com:7000/home/

* HomeテーブルにHomeを追加
  - GET http://imaginaryshort.com:7000/home/add?name={value}

* Userテーブルの情報を出力
  - GET http://imaginaryshort.com:7000/user/

* HomeテーブルにHomeを追加
  - GET http://imaginaryshort.com:7000/user/add?id={value}&name={value}&order={value}
