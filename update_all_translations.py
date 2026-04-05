#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import xml.etree.ElementTree as ET
import os
from pathlib import Path

# Tüm diller için kapsamlı çeviriler
translations = {
    'values-da': {  # Dansk
        'mother_short_desc': 'Det stille bøn i mit hjerte er altid for dig, mor.',
        'father_desc_short': 'Din skygge er nok, far, du giver mig styrke ved hvert trin.',
        'sibling_desc_short': 'Uendelig venskab i et lille hjerte: Min søskende.',
        'sweatheart_desc_short': 'Når du smiler, tier verden, kun mit hjerte taler.',
        'teacher_desc_short': 'Du gav et bogstav, vejledte vejen hele livet.',
        'friend_desc_short': 'Ægte venskab er at forstå selv i stilhed.',
        'reminder_message_text': 'Den begivenhed, du oprettede, nærmer sig',
        'celebrations_list_title': 'Særlige dage',
        'error_fill_required_fields': 'Udfyld venligst alle påkrævede felter',
        'label_event_information': 'Begivenhedsinformation',
        'label_notification_timing': 'Hvornår vil du modtage meddelelser?',
        'placeholder_special_request': 'For eksempel ønsker jeg ikke en bog eller smartwatch osv.',
        'error_unknown': 'Der opstod en ukendt fejl',
        'user_info_missing': 'Brugerinformation mangler',
        'user_info_missing_desc': 'Tilføj dine personlige oplysninger\nfor bedre forslag',
        'personal_info_title': 'Personlig information',
        'social_features_title': 'Sociale funktioner',
        'track_favorite_gifts': 'Du kan spore dine yndlings\ngaver her'
    },
    'values-nl': {  # Nederlands
        'mother_short_desc': 'Het stille gebed in mijn hart is altijd voor jou, moeder.',
        'father_desc_short': 'Je schaduw is genoeg, vader, je geeft me kracht bij elke stap.',
        'sibling_desc_short': 'Oneindige vriendschap in een klein hart: Mijn broer/zus.',
        'sweatheart_desc_short': 'Als je glimlacht, zwijgt de wereld, alleen mijn hart spreekt.',
        'teacher_desc_short': 'Je gaf een letter, je wees het pad voor het leven.',
        'friend_desc_short': 'Echte vriendschap is begrijpen zelfs in stilte.',
        'reminder_message_text': 'De gebeurtenis die je hebt aangemaakt nadert',
        'celebrations_list_title': 'Speciale dagen',
        'error_fill_required_fields': 'Vul alle verplichte velden in',
        'label_event_information': 'Gebeurtenisinformatie',
        'label_notification_timing': 'Wanneer wil je meldingen ontvangen?',
        'placeholder_special_request': 'Bijvoorbeeld, ik wil geen boek of smartwatch enz.',
        'error_unknown': 'Er is een onbekende fout opgetreden',
        'user_info_missing': 'Gebruikersinformatie ontbreekt',
        'user_info_missing_desc': 'Voeg je persoonlijke informatie toe\nvoor betere suggesties',
        'personal_info_title': 'Persoonlijke informatie',
        'social_features_title': 'Sociale functies',
        'track_favorite_gifts': 'Je kunt je favoriete\ncadeaus hier volgen'
    },
    'values-fr': {  # Français
        'mother_short_desc': 'La plus silencieuse prière de mon cœur est toujours pour toi, maman.',
        'father_desc_short': 'Ton ombre me suffit, papa, tu me donnes de la force à chaque pas.',
        'sibling_desc_short': 'Une amitié infinie dans un petit cœur : Mon frère/Ma sœur.',
        'sweatheart_desc_short': 'Quand tu souris, le monde se tait, seul mon cœur parle.',
        'teacher_desc_short': 'Tu as donné une lettre, tu as montré le chemin pour la vie.',
        'friend_desc_short': 'L\'amitié véritable, c\'est se comprendre même dans le silence.',
        'reminder_message_text': 'L\'événement que tu as créé approche',
        'celebrations_list_title': 'Jours spéciaux',
        'error_fill_required_fields': 'Veuillez remplir tous les champs obligatoires',
        'label_event_information': 'Informations sur l\'événement',
        'label_notification_timing': 'Quand souhaitez-vous recevoir les notifications?',
        'placeholder_special_request': 'Par exemple, je ne veux pas de livre ou de montre intelligente, etc.',
        'error_unknown': 'Une erreur inconnue s\'est produite',
        'user_info_missing': 'Informations utilisateur manquantes',
        'user_info_missing_desc': 'Ajoutez vos informations personnelles\npour obtenir de meilleures suggestions',
        'personal_info_title': 'Informations personnelles',
        'social_features_title': 'Fonctionnalités sociales',
        'track_favorite_gifts': 'Vous pouvez suivre vos cadeaux\npréférés ici'
    },
    'values-el': {  # Ελληνικά
        'mother_short_desc': 'Η πιο σιωπηρή προσευχή στην καρδιά μου είναι πάντα για σένα, μαμά.',
        'father_desc_short': 'Η σκιά σου είναι αρκετή, μπαμπά, μου δίνεις δύναμη σε κάθε βήμα.',
        'sibling_desc_short': 'Άπειρη φιλία σε μια μικρή καρδιά: Αδερφός/Αδελφή.',
        'sweatheart_desc_short': 'Όταν χαμογελάς, ο κόσμος σιωπά, μόνο η καρδιά μου μιλάει.',
        'teacher_desc_short': 'Έδωσες ένα γράμμα, έδειξες το δρόμο για τη ζωή.',
        'friend_desc_short': 'Η αληθινή φιλία είναι να καταλαβαίνεις ακόμα και στη σιωπή.',
        'reminder_message_text': 'Το γεγονός που δημιούργησες πλησιάζει',
        'celebrations_list_title': 'Ειδικές ημέρες',
        'error_fill_required_fields': 'Συμπληρώστε όλα τα υποχρεωτικά πεδία',
        'label_event_information': 'Πληροφορίες εκδήλωσης',
        'label_notification_timing': 'Πότε θέλετε να λάβετε ειδοποιήσεις;',
        'placeholder_special_request': 'Για παράδειγμα, δεν θέλω ένα βιβλίο ή έξυπνο ρολόι κ.λπ.',
        'error_unknown': 'Παρουσιάστηκε άγνωστο σφάλμα',
        'user_info_missing': 'Λείπουν πληροφορίες χρήστη',
        'user_info_missing_desc': 'Προσθέστε τα προσωπικά σας στοιχεία\nγια καλύτερες προτάσεις',
        'personal_info_title': 'Προσωπικές πληροφορίες',
        'social_features_title': 'Κοινωνικές δυνατότητες',
        'track_favorite_gifts': 'Μπορείτε να παρακολουθήσετε τα αγαπημένα\nσας δώρα εδώ'
    },
    'values-hu': {  # Magyar
        'mother_short_desc': 'A szívem legcsendesebb imája mindig érted, anyám.',
        'father_desc_short': 'Az árnyékod elég, apám, erőt adsz minden lépésemnél.',
        'sibling_desc_short': 'Végtelen barátság egy kicsi szívben: Testvérem.',
        'sweatheart_desc_short': 'Amikor mosolyogsz, a világ hallgat, csak a szívem beszél.',
        'teacher_desc_short': 'Egy betűt adtál, az életre mutattad az utat.',
        'friend_desc_short': 'Az igazi barátság az, amikor megértesz még a csendben is.',
        'reminder_message_text': 'A létrehozott esemény közeledik',
        'celebrations_list_title': 'Különleges napok',
        'error_fill_required_fields': 'Kérjük, töltse ki az összes kötelező mezőt',
        'label_event_information': 'Esemény információk',
        'label_notification_timing': 'Mikor szeretnél értesítéseket kapni?',
        'placeholder_special_request': 'Például nem szeretnék könyvet vagy okosórát stb.',
        'error_unknown': 'Ismeretlen hiba történt',
        'user_info_missing': 'Hiányzik a felhasználói információ',
        'user_info_missing_desc': 'Adja meg személyes adatait\njobb javaslatokért',
        'personal_info_title': 'Személyes adatok',
        'social_features_title': 'Közösségi funkciók',
        'track_favorite_gifts': 'Nyomon követheted kedvenc\najándékaidat itt'
    },
    'values-it': {  # Italiano
        'mother_short_desc': 'La preghiera più silenziosa nel mio cuore è sempre per te, mamma.',
        'father_desc_short': 'La tua ombra mi basta, papà, mi dai forza ad ogni passo.',
        'sibling_desc_short': 'Un\'amicizia infinita in un piccolo cuore: Mio fratello/Mia sorella.',
        'sweatheart_desc_short': 'Quando sorridi, il mondo tace, solo il mio cuore parla.',
        'teacher_desc_short': 'Hai dato una lettera, hai mostrato il cammino per la vita.',
        'friend_desc_short': 'L\'vera amicizia è capirsi anche nel silenzio.',
        'reminder_message_text': 'L\'evento che hai creato si avvicina',
        'celebrations_list_title': 'Giorni speciali',
        'error_fill_required_fields': 'Si prega di compilare tutti i campi obbligatori',
        'label_event_information': 'Informazioni evento',
        'label_notification_timing': 'Quando desideri ricevere notifiche?',
        'placeholder_special_request': 'Ad esempio, non voglio un libro o uno smartwatch ecc.',
        'error_unknown': 'Si è verificato un errore sconosciuto',
        'user_info_missing': 'Informazioni utente mancanti',
        'user_info_missing_desc': 'Aggiungi le tue informazioni personali\nper suggerimenti migliori',
        'personal_info_title': 'Informazioni personali',
        'social_features_title': 'Funzioni sociali',
        'track_favorite_gifts': 'Puoi tracciare i tuoi regali\npreferiti qui'
    },
    'values-ru': {  # Русский
        'mother_short_desc': 'Самая тихая молитва в моем сердце всегда за тебя, мама.',
        'father_desc_short': 'Твоя тень достаточна, папа, ты даешь мне силу на каждом шаге.',
        'sibling_desc_short': 'Бесконечная дружба в маленьком сердце: Мой брат/Моя сестра.',
        'sweatheart_desc_short': 'Когда ты улыбаешься, мир молчит, только мое сердце говорит.',
        'teacher_desc_short': 'Ты дал букву, показал путь на всю жизнь.',
        'friend_desc_short': 'Настоящая дружба — это понимание даже в молчании.',
        'reminder_message_text': 'Созданное вами событие приближается',
        'celebrations_list_title': 'Специальные дни',
        'error_fill_required_fields': 'Пожалуйста, заполните все обязательные поля',
        'label_event_information': 'Информация о событии',
        'label_notification_timing': 'Когда вы хотите получать уведомления?',
        'placeholder_special_request': 'Например, я не хочу книгу или умные часы и т.д.',
        'error_unknown': 'Произошла неизвестная ошибка',
        'user_info_missing': 'Отсутствует информация пользователя',
        'user_info_missing_desc': 'Добавьте свою личную информацию\nдля лучших предложений',
        'personal_info_title': 'Личная информация',
        'social_features_title': 'Социальные функции',
        'track_favorite_gifts': 'Вы можете отследить свои любимые\nподарки здесь'
    }
}

base_path = 'app/src/main/res'
total_updated = 0

for lang_folder, lang_strings in translations.items():
    file_path = os.path.join(base_path, lang_folder, 'strings.xml')

    if not os.path.exists(file_path):
        print(f'✗ {lang_folder}: Dosya bulunamadı')
        continue

    try:
        # XML'i oku
        tree = ET.parse(file_path)
        root = tree.getroot()

        # Var olan string isimleri kontrol et
        existing_names = {elem.get('name') for elem in root.findall('string')}

        added = 0
        # Eksik stringleri ekle
        for name, value in lang_strings.items():
            if name not in existing_names:
                new_elem = ET.Element('string')
                new_elem.set('name', name)
                new_elem.text = value
                root.append(new_elem)
                added += 1
                total_updated += 1

        # Dosyayı kaydet
        tree.write(file_path, encoding='utf-8', xml_declaration=True)
        print(f'✓ {lang_folder}: {added} yeni string eklendi')

    except Exception as e:
        print(f'✗ {lang_folder}: Hata - {str(e)}')

print(f'\n✓ Toplam {total_updated} string tüm dillere eklendi!')

