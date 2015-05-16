
import sys

def readCSV(filename, dataType=None):
    with open(filename, 'r') as f:
        line = f.readline() # first line: column name
        colNameMap = {c.strip():i for i, c in enumerate(line.strip().split(','))}

        data = list()
        for line in f:
            entry = line.strip().split(',')
            if dataType != None:
                assert len(dataType) == len(entry)
                row = list()
                for i, e in enumerate(entry):
                    if dataType[i] == 'int':
                        row.append(int(e))
                    elif dataType[i] == 'float':
                        row.append(float(e))
                    else:
                        row.append(e.strip())
            else:
                row = entry
            data.append(row)
    return (colNameMap, data)

def writeCSV(colNameMap, data, outfile=sys.stdout):
    for colName, i in sorted(colNameMap.items(), key=lambda x:x[1]):
        if i == len(colNameMap)-1:
            print(colName, end='\n', file=outfile)
        else:
            print(colName, end=',', file=outfile)
    
    for row in data:
        for i, e in enumerate(row):
            if i == len(row)-1:
                print(e, end='\n', file=outfile)
            else:
                print(e, end=',', file=outfile)
    return 


# colNameMap: name -> index 
def sortByColumn(data, colNameMap, sortColumn, reverse=True):
    if sortColumn not in colNameMap:
        return data #do nothing
    else:
        data.sort(key=lambda x:x[colNameMap[sortColumn]], reverse=reverse)
    return data

def allowData(data, colNameMap, filterColumn, allow, type='string'):
    if filterColumn not in colNameMap:
        return data 

    ci = colNameMap[filterColumn]
    newData = None
    if type == 'string':
        newData = [row for row in data if row[ci].strip() in allow]
    elif type == 'int':
        if type(allow) == int: # single value
            newData = [row for row in data if row[ci] == allow]
        elif (type(allow) == tuple or type(allow) == list) and len(allow) == 2: #min max
            newData = [row for row in data if row[ci] >= allow[0] and row[ci] <= allow[1]]
    elif type == 'float':
        if type(allow) == float: # single value
            newData = [row for row in data if floatEq(row[ci], allow)]
        elif (type(allow) == tuple or type(allow) == list) and len(allow) == 2: #min max
            newData = [row for row in data if row[ci] >= allow[0] and row[ci] <= allow[1]]
    return newData


# merge the rows with the same setting, average the results
# keyPrefixNum: the number of columns to be merged as key
def mergeRows(data, colNameMap, keyPrefixNum):
    keyData = dict()
    print(len(data[0]))
    for d in data:
        key = tuple(d[0:keyPrefixNum])
        if key not in keyData:
            keyData[key] = list()
        keyData[key].append(d)
    
    newData = list()
    for key, dList in keyData.items():
        #print('key:', key)
        #print('num:', len(dList))
        avgD = [0.0 for i in range(0, len(colNameMap) - keyPrefixNum)]
        for d in dList:
            for i, e in enumerate(d[keyPrefixNum:len(colNameMap)]):
                if type(e) == int or type(e) == float:
                    avgD[i] += e
        for i in range(0, len(colNameMap) - keyPrefixNum):
            avgD[i] /= len(dList)
        newRow = list(key)
        newRow.extend(avgD)
        #print(newRow)
        newData.append(newRow)
    return newData

def floatEq(f1, f2):
    return fabs(f1 - f2) < 1e-10


PKTypeColName ='PKTYPE'
PKTypeMapping = {
    "03": 0
}

PKType1ColName = 'PKTYPE1'
PKType1Mapping = {   
    "殘障機車" : 0, 
    "身心殘障機車" : 0,
    "身心殘障專用(汽)": 1, 
    "身心殘障專用(汽": 1,
    "身障機車": 0, 
    "殘汽": 1,
    "身心殘障專用": 2, 
    "身心障礙格": 2, 
    "殘機": 0, 
    "身心殘障專用(機":0, 
    "身障小型車":1, 
    "殘障汽車":1, 
    "身心障礙":2, 
    "身障汽車":1, 
    "身心障礙(汽)":1
}

['', '小型車', '身障小型車', '身心障礙', '身心殘障專用(汽)', '裝卸貨車', '身心殘障專用', '機車', '殘障機車', '機車停車彎', '殘汽', '身心殘障專用(汽', '限時停車', '身障汽車', '殘障汽車', '身心障礙(汽)', '小汽車']


PKType1Name = ["身心障礙專用機車位", "身心障礙專用汽車位", "身心障礙專用"]

if __name__ == '__main__':
    if len(sys.argv) != 3:
        print('Usage:', sys.argv[0], 'InCSV OutCSV', file=sys.stderr)
        exit(-1)

    inCSV = sys.argv[1]
    outCSV = sys.argv[2]

    (colNameMap, data) = readCSV(inCSV)

    newData = allowData(data, colNameMap, PKTypeColName, 
            allow=set(PKTypeMapping.keys()), type='string')
    print('# disable parking data(PKTYPE1):', len(newData))
    idSet1 = set([row[0] for row in newData])
    print('# idSet1:', len(idSet1))

    newData1 = allowData(data, colNameMap, PKType1ColName, 
            allow=set(PKType1Mapping.keys()), type='string')
    print('# disable parking data(PKTYPE):', len(newData1))
    idSet2 = set([row[0] for row in newData1])
    print('# idSet2:', len(idSet2))

    print('intersect:', len(idSet1 & idSet2))
    print(len(idSet2 - idSet1))

    newData2 = set([tuple(row) for row in newData]) | set([tuple(row) for row in newData1])
    newData2 = [list(row) for row in newData2]

    #typeSet = set()
    #for row in newData:
    #    typeSet.add(row[colNameMap[PKType1ColName]])
    
    #print(typeSet)
    with open(outCSV, 'w') as f:
        writeCSV(colNameMap, newData2, f)

    
