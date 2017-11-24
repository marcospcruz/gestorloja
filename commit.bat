
set mensagemCommit=%1
echo mensagem: %mensagemCommit%

rem * * *  mvn eclipse:clean

sleep 10

git add --all .

git commit -m %mensagemCommit%

git push origin master

mvn eclipse:eclipse