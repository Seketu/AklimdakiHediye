#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import re
import os

files_to_fix = [
    'app/src/main/res/values-da/strings.xml',
    'app/src/main/res/values-nl/strings.xml',
    'app/src/main/res/values-fr/strings.xml',
    'app/src/main/res/values-el/strings.xml',
    'app/src/main/res/values-hu/strings.xml',
    'app/src/main/res/values-it/strings.xml',
    'app/src/main/res/values-ru/strings.xml',
]

fixed_count = 0

for file_path in files_to_fix:
    if not os.path.exists(file_path):
        print(f'✗ {file_path}: Dosya bulunamadı')
        continue

    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        # Son satırda multiple string elements olup olmadığını kontrol et
        # Pattern: ></resources> öncesinde string elementleri biraraya yazılmış
        if '><string name=' in content and '></resources>' in content:
            # Tüm dosyayı XML olarak oku
            import xml.etree.ElementTree as ET
            tree = ET.parse(file_path)
            root = tree.getroot()

            # Dosyayı proper şekilde kaydet
            tree.write(file_path, encoding='utf-8', xml_declaration=True)
            print(f'✓ {file_path}: Format düzeltildi')
            fixed_count += 1
        else:
            print(f'✓ {file_path}: Zaten düzenli')

    except Exception as e:
        print(f'✗ {file_path}: Hata - {str(e)}')

print(f'\n✓ Toplam {fixed_count} dosya düzeltildi!')

