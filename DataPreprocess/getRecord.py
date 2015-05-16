#!/usr/bin/env python3 
import shapefile
import sys

if __name__ == '__main__':
    if len(sys.argv) != 3:
        print('Usage:', sys.argv[0], 'dataPath record_CSV', file=sys.stderr)
        exit(-1)

    dataPath = sys.argv[1]
    recordCSV = sys.argv[2]

    sf = shapefile.Reader(dataPath)
    #sh = shapefile.Reader(shp=shp_f, dbf=dbf_f)

    with open(recordCSV, 'w') as f:
        fieldNum = len(sf.fields) - 1
        for field in sf.fields[1:-1]:
            print(field[0], end=',', file=f)
        print(field[-1], file=f)
        #print('LL_WGS84_X, LL_WGS84_Y, RU_WGS84_X, RU_WGS84_Y', file=f)
        for record in sf.iterRecords():
            if len(record) != fieldNum:
                print(len(record))
            for i, d in enumerate(record):
                if type(d) == bytes:
                    print('', end='', file=f)
                elif type(d) == str:
                    print(d.replace(',', ' ').replace('\n', ''), end='', file=f)
                else:
                    print(d, end='', file=f)
                if i < len(record) - 1:
                    print('', end=',', file=f)
            print('', file=f)
