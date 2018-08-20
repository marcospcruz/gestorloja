echo off
cls
set mensagemCommit=%1
rem * * * echo mensagem: %mensagemCommit%

call mvn eclipse:clean

rem * * * sleep 5
ping 127.0.0.1 -n 6 > nul

git add --all .

git commit -m %mensagemCommit%

rem * * * git push origin master
git push https://github.com/marcospcruz/gestorloja.git

call mvn eclipse:eclipse

rem * * * fim