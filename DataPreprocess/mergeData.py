#!/usr/bin/env python3

import sys

if __name__ == '__main__':
    if len(sys.argv) != 4:
        print('Usage:', sys.argv[0], 'record_CSV position_CSV outMerge_CSV', file=sys.stderr)
        exit(-1)
    
    recordCSV = sys.argv[1]
    posCSV = sys.argv[2]
    outCSV = sys.argv[3]

    with open(recordCSV, 'r') as f1, open(posCSV, 'r') as f2, open(outCSV, 'w') as fout:
        for line1, line2 in zip(f1, f2):
            print(line1.strip(), line2.strip(), sep=',', file=fout)

        line1 = f1.readline()
        line2 = f2.readline()
        print('Line1:', line1)
        print('Line2:', line2)
        if line1 or line2:
            print('File Inconsistent')
            
        
