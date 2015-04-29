# TaipeiParkingMap 台北市路邊停車格位資料處理

簡介
------------------------------------
* 這個專案的目的是去處理台北市Open Data-「[臺北市路邊停車格位](http://data.taipei.gov.tw/opendata/apply/NewDataContent?oid=DC46123A-8142-41EC-AC2F-E608B59DFEFF)」資料。
* 資料格式: [shapefile](http://zh.wikipedia.org/zh-tw/Shapefile)，可用[pyshp](https://code.google.com/p/pyshp/)讀取, 其中位置資訊為[TWD67二分帶](http://www.sunriver.com.tw/grid_tm2.htm)格式。
* 本專案將原始資料中的Record資訊(即停車格欄位資訊)擷取出來，再將Shape資訊(包含停車格之多邊形(points)及包覆之矩形(bbox)位置資訊等)中bbox資訊(TWD67二分帶格式)擷取出來並轉換為[WGS84](http://zh.wikipedia.org/wiki/WGS84)座標((即我們一般理解的經緯度資訊，可用於google map)。
* 轉換方式為: TWD67二分帶格式 ----[四參數轉換](http://gis.thl.ncku.edu.tw/coordtrans/coordtrans.aspx)---> TWD97二分帶格式 ---[pyproj](https://pypi.python.org/pypi/pyproj)---> WGS84格式

相關資源

如何打開一個utf-8編碼的csv檔案
------------------------------------
1. 打開excel
2. 上方選「資料」-> 選「從文字檔」
3. 選擇該csv檔(e.g. park01.csv)
4. 「分隔符號」打勾，檔案原始格式選 「65001: Unicode(utf-8)」，下一步
5.  分隔符號只選「逗號」，一直下一步就行了
* 注意，資料有可能無法全部載入
