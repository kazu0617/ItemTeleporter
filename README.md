# ItemTeleporter
## なんぞこれ
- プレイヤーが指定の座標、プレイヤーにドロップするアイテムを追加する。
- 悪ふざけあり。

## コマンドについて
- /itemteleporter --- コマンドの一覧を表示。
- /itemteleporter player <player> --- 指定のプレイヤーにテレポート。<player>にプレイヤー名を記入。
- /itemteleporter location <x> <y> <z> --- 指定の座標にテレポート。<x>、<y>、<z>に座標を記入。
- /itemteleporter random -- ランダムな座標にテレポート。ほぼ100%死に至る。
- /itemteleporter getitem -- テレポートするItemを取得する。

## Permissonについて
- teleport.toPlayers --- 指定のプレイヤーにテレポートするのを許可する。 default: true
- teleport.toLocation --- 指定の座標にテレポートするのを許可する。 default: true
- teleport.getItem --- テレポートするItemを取得するのを許可する。 default: false
- teleport.admin --- 上3つの権限をもらう。 default: op

## Todo
- テレポートするItemを複製できないようにする(Loreで指定可能？)
- x,y,zをその都度入力できるようにする。
- 対話式にする(xを入力して下さい…)。

## 使用しているソース
- ucceyさんが制作しているUndineMailerのソースを一部改変して使用させて頂いてます。感謝。
- このプログラムの原型を作ったのはkotmw0701です。なので、authorにkotmw0701を追加しています。

## 最後に
- ソースって何派ですか。自分はウスターソース派です。
