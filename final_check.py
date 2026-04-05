#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import xml.etree.ElementTree as ET

langs = [
    ('values', 'Türkçe'),
    ('values-en-rUS', 'İngilizce'),
    ('values-de-rDE', 'Almanca'),
    ('values-fr', 'Fransızca'),
    ('values-el', 'Yunanca'),
    ('values-hu', 'Macarca'),
    ('values-it', 'İtalyanca'),
    ('values-ru', 'Rusça'),
    ('values-ar', 'Arapça'),
    ('values-bg', 'Bulgarca'),
    ('values-da', 'Danca'),
    ('values-nl', 'Hollandaca')
]

print("=== FINAL DURUM ===\n")
min_count = float('inf')

for folder, name in langs:
    file_path = f'app/src/main/res/{folder}/strings.xml'
    try:
        tree = ET.parse(file_path)
        root = tree.getroot()
        count = len(root.findall('string'))
        min_count = min(min_count, count)
        print(f"✓ {name:15} ({folder:20}): {count:3} strings")
    except Exception as e:
        print(f"✗ {name:15} ({folder:20}): ERROR - {e}")

print(f"\nMinimum: {min_count} strings")
if min_count >= 260:
    print("✓ TÜM DİLLER TAMAMLANDI!")
else:
    print(f"⚠  {260 - min_count} string eksik")

