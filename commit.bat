
set mensagemCommit=%1
echo mensagem: %mensagemCommit%

call mvn eclipse:clean

rem * * * sleep 10

git add --all .

git commit -m %mensagemCommit%

git push origin master

call mvn eclipse:eclipse