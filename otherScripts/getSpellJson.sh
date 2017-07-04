#!/bin/bash

url="http://www.dnd5eapi.co/api/spells/"

spellNum=305

rm spellData.json
echo "{" >> spellData.json
for (( i=1; i<=305; i++ ))
do
    spellName=\"`curl -s ${url}${i} | jq -r '.name'`\";
    echo ${spellName}":" >> spellData.json;
    curl ${url}${i} >> spellData.json;
    echo "," >> spellData.json;
    #curl ${url}${i} >> spells/${spellName}.json
done
echo "}" >> spellData.json
