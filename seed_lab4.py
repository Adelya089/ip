"""Заполнение тестовыми данными через существующий REST API (отдельно от миграций)."""
import json
from urllib.request import Request, urlopen

BASE = 'http://localhost:8080/api/1.0'

def post(path, data):
    body = json.dumps(data).encode('utf-8')
    req = Request(BASE + '/' + path, data=body, headers={'Content-Type':'application/json'}, method='POST')
    with urlopen(req, timeout=20) as res:
        return json.loads(res.read())['id']

def main():
    genres = [post('genres', {'name':f'Тестовый жанр {i}'}) for i in range(1, 11)]
    artists = [post('artists', {'name':f'Тестовый исполнитель {i}', 'country':'Россия', 'genreId':genres[i%len(genres)]}) for i in range(500)]
    albums = [post('albums', {'title':f'Тестовый альбом {i}', 'year':2020, 'artistId':artists[i%len(artists)], 'trackCount':2, 'cover':''}) for i in range(1500)]
    for i in range(3000):
        post('tracks', {'title':f'Тестовый трек {i}', 'duration':180, 'albumId':albums[i//2]})
        if i%500==0: print('Создано треков:', i+1)
    print('Готово: 10 жанров, 500 исполнителей, 1500 альбомов, 3000 треков.')

if __name__ == '__main__': main()
