#!/usr/bin/env python3
import csv
import json
import re
from datetime import datetime

INPUT = 'misc/kibana/LogReport.csv'
OUTPUT = 'misc/kibana/sample-logs.jsonl'
MAX_LINES = 2000

HTTP_METHODS = {'GET','POST','PUT','DELETE','PATCH','OPTIONS','HEAD'}

def parse_ts(s):
    s = s.strip('"')
    # example: Apr 27, 2026 @ 18:21:26.977
    try:
        s2 = s.replace(' @ ', ' ')
        return datetime.strptime(s2, '%b %d, %Y %H:%M:%S.%f').isoformat()+'Z'
    except Exception:
        return s

def find_method_path_status_duration(row):
    method = None
    path = None
    status = None
    duration = None
    for i, v in enumerate(row):
        if v in HTTP_METHODS:
            method = v
            # path may be next field or next+1
            if i+1 < len(row):
                candidate = row[i+1]
                if candidate.startswith('/') or candidate.startswith('http'):
                    path = candidate
            # status may be following fields (numeric 100-599)
            for j in range(i, min(i+6, len(row))):
                try:
                    val = int(re.sub('[^0-9]','', row[j]))
                    if 100 <= val <= 599:
                        status = val
                        break
                except Exception:
                    continue
            break
    # duration: find a small integer earlier in row (<=10000)
    for v in row:
        try:
            val = int(re.sub('[^0-9]','', v))
            if 0 <= val <= 10000:
                # heuristics: duration likely < 5000 and not 1/flags
                if duration is None or val < duration:
                    duration = val
        except Exception:
            continue
    # service: look for 'front' or 'back'
    service = None
    for v in row:
        if v in ('front','back'):
            service = v
            break
    # level: INFO/WARN/ERROR
    level = None
    for v in row:
        if v in ('INFO','WARN','ERROR','DEBUG'):
            level = v
            break
    # message: take any field containing 'HTTP request' or 'RepositoryOperation' or 'created'
    message = None
    for v in row:
        if isinstance(v, str) and ('HTTP request' in v or 'RepositoryOperation' in v or 'created' in v or 'completed' in v or 'timeout' in v):
            message = v
            break
    return method, path, status, duration, level, message, service

def main():
    out_count = 0
    with open(INPUT, newline='', encoding='utf-8', errors='replace') as f_in, open(OUTPUT, 'w', encoding='utf-8') as f_out:
        reader = csv.reader(f_in)
        for row in reader:
            if not row:
                continue
            ts = parse_ts(row[0])
            method, path, status, duration, level, message, service = find_method_path_status_duration(row)
            doc = {
                '@timestamp': ts,
                'level': level or 'INFO',
                'message': message or '',
                'http_method': method or '-',
                'http_path': path or '-',
                'http_status': status or 0,
                'http_duration_ms': duration or 0,
                'service': service or 'microcrm'
            }
            f_out.write(json.dumps(doc, ensure_ascii=False) + '\n')
            out_count += 1
            if out_count >= MAX_LINES:
                break
    print(f'Wrote {out_count} docs to {OUTPUT}')

if __name__ == '__main__':
    main()
