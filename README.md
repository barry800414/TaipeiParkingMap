# TaipeiParkingMap 台北市路邊停車格位資料處理

## 簡介
* 這個專案的目的是去處理台北市Open Data-「[臺北市路邊停車格位](http://data.taipei.gov.tw/opendata/apply/NewDataContent?oid=DC46123A-8142-41EC-AC2F-E608B59DFEFF)」資料，將裡面停車格資訊取出，經緯度的部分轉為WGS84格式，並存為csv檔。
* 資料格式: [shapefile](http://zh.wikipedia.org/zh-tw/Shapefile)，可用[pyshp](https://code.google.com/p/pyshp/)讀取, 其中位置資訊應該為[TWD67二分帶](http://www.sunriver.com.tw/grid_tm2.htm)格式。
* 本專案將原始資料中的Record資訊(即停車格欄位資訊)擷取出來，再將Shape資訊(包含停車格之多邊形(points)及包覆之矩形(bbox)位置資訊等)中bbox資訊(TWD67二分帶格式)擷取出來並轉換為[WGS84](http://zh.wikipedia.org/wiki/WGS84)座標(即我們一般理解的經緯度資訊，可用於google map)。
* 轉換方式為: TWD67二分帶格式 ----[四參數轉換](http://gis.thl.ncku.edu.tw/coordtrans/coordtrans.aspx)---> TWD97二分帶格式 ---[pyproj](https://pypi.python.org/pypi/pyproj)---> WGS84格式

* 處理完之資料: [點此下載](http://140.112.187.33/~r02922010/park01.csv)  2016/5/25更新

## 最低需求 (Prerequisites)
* Python3
* pyproj & proj  (請使用pip3安裝，安裝指令如 pip3 install -U pyproj)

## 執行方式
#### 從shape file(副檔名為.shp)中讀取Record資訊(停車格欄位資訊)
        python3 getRecord.py shapeFile(.shp) out_Record_CSV

#### 從shape file 取出停車格經緯度資訊，並轉為 WGS84 格式
        python3 getPosition.py shapeFile(.shp) out_Position_CSV(WGS84)

#### 將 Record資訊與經緯度資訊合併成一個csv檔
        python3 mergeData.py record_CSV position_CSV out_Merge_CSV


## 相關資源
* [pyshp](https://code.google.com/p/pyshp/) - the package for reading shapefile
* [pyproj](https://pypi.python.org/pypi/pyproj) - the python wrapper for [proj](https://trac.osgeo.org/proj/), which is a powerful package for GIS system 
* [TWD67-TWD97-WGS84座標轉換](http://gis.thl.ncku.edu.tw/coordtrans/coordtrans.aspx) - 成大水工所網頁
* [TWD97-WGS84 轉換using pyproj](http://blog.changyy.org/2012/11/twd67-twd97-wgs84.html)


## 遇到的問題
1. record讀取的時候，編碼大部分為big5，因此必須修改shapefile.py，它預設為utf-8編碼。
2. record也有其他可能之編碼，目前嘗試utf-8, big5, cp950, cp932，尚有無法解碼之文字(共10幾筆)，將其捨棄。

## 如何打開一個utf-8編碼的csv檔案
1. 打開excel
2. 上方選「資料」-> 選「從文字檔」
3. 選擇該csv檔(e.g. park01.csv)
4. 「分隔符號」打勾，檔案原始格式選 「65001: Unicode(utf-8)」，下一步
5.  分隔符號只選「逗號」，一直下一步就行了
* 注意，資料有可能無法全部載入


連絡我
------------------------------------
* barry800414 AT gmail.com
