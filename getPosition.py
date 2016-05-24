#!/usr/bin/env python
from __future__ import print_function
import shapefile
import sys
import os
from pyproj import *


A = 0.00001549
B = 0.000006521
p = Proj(r"+proj=tmerc +ellps=GRS80 +lon_0=121 +x_0=250000 +k=0.9999")
def TWD97ToTWD67(X97, Y97):
    X67=X97-807.8-A*X97-B*Y97
    Y67=Y97+248.6-A*Y97-B*X97
    return (X67, Y67)

def TWD67ToTWD97(X67, Y67):
    X97=X67+807.8+A*X67+B*Y67
    Y97=Y67-248.6+A*Y67+B*X67
    return (X97, Y97)

def TWD97ToWGS84(X97, Y97):
    ''' 
    cmd = 'echo %.15f %.15f | proj -I +proj=tmerc +ellps=GRS80 +lon_0=121 +x_0=250000 +k=0.9999 -f "%%.15f"' %(X97, Y97)
    result = os.popen(cmd).read().strip()
    result = result.split()
    WGS84_X = float(result[0])
    WGS84_Y = float(result[1])
    '''
    (WGS84_X, WGS84_Y) = p(X97, Y97, inverse=True)
    return (WGS84_X, WGS84_Y)

# testing case
'''
(x, y) = (306464.28974748246, 2772824.9946845993)
(x97, y97) = TWD67ToTWD97(x, y)
TWD97ToWGS84(x97, y97)
'''


if __name__ == '__main__':
    if len(sys.argv) != 3:
        print('Usage:', sys.argv[0], 'shapeFile(.shp) out_Position_CSV(WGS84)', file=sys.stderr)
        exit(-1)
    dataPath = sys.argv[1]
    outCSV = sys.argv[2]
    sf = shapefile.Reader(dataPath)
   
    with open(outCSV, 'w') as f:
        print('LL_WGS84_X, LL_WGS84_Y, RU_WGS84_X, RU_WGS84_Y, C_WGS84_X, C_WGS84_Y', file=f)
        for shape in sf.iterShapes():
            bbox = shape.bbox
            (LL_X, LL_Y, RU_X, RU_Y) = bbox
            C_X = (LL_X + RU_X) / 2.0
            C_Y = (LL_Y + RU_Y) / 2.0
            (LL_X97, LL_Y97) = TWD67ToTWD97(LL_X, LL_Y)
            (RU_X97, RU_Y97) = TWD67ToTWD97(RU_X, RU_Y)
            (C_X97, C_Y97) = TWD67ToTWD97(C_X, C_Y)
            (LL_WGS84_X, LL_WGS84_Y) = TWD97ToWGS84(LL_X97, LL_Y97)
            (RU_WGS84_X, RU_WGS84_Y) = TWD97ToWGS84(RU_X97, RU_Y97)
            (C_WGS84_X, C_WGS84_Y) = TWD97ToWGS84(C_X97, C_Y97)
            print(LL_WGS84_X, LL_WGS84_Y, RU_WGS84_X, RU_WGS84_Y, C_WGS84_X, C_WGS84_Y, sep=',', file=f)

