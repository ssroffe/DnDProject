import json

with open("spellData.json","r") as f:
    data = json.load(f)

tmpData = data
for key in data:
    try:
        for i in range(len(tmpData[key]['higher_level'])):
            tmpData[key]['desc'].append(tmpData[key]['higher_level'][i])
        tmpData[key].pop('higher_level')
    except KeyError:
        print(tmpData[key]['name'])

for key in tmpData:
    try:
        tmpData[key]['components'].append(tmpData[key]['material'])
        tmpData[key].pop('material')
    except AttributeError:
        tmpData[key]['components'] = [tmpData[key]['components']]
        try:
            tmpData[key]['components'].append(tmpData[key]['material'])
            tmpData[key].pop('material')
        except KeyError:
            print(tmpData[key]['name'])
    except KeyError:
        print(tmpData[key]['name'])
        
newData = [ { "name": tmpData[key]['name'], "desc":tmpData[key]['desc'], "range": tmpData[key]['range'], 'components': tmpData[key]['components'],
          "ritual": tmpData[key]['ritual'], "duration": tmpData[key]['duration'], "concentration": tmpData[key]['concentration'], "casting_time": tmpData[key]['casting_time'],
          "level": tmpData[key]['level'], "school": tmpData[key]['school']['name'], "clss": [tmpData[key]['classes'][i]['name'] for i in range(len(tmpData[key]['classes']))]
          } for key in tmpData] 

with open("spells.json","w") as outfile:
    json.dump(newData,outfile)
