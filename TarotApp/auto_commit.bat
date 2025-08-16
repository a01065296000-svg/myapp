@echo off
cd /d "C:\Users\seese\AndroidStudioProjects\TarotApp"
echo TarotApp 자동 백업 시작...
git add .
git commit -m "TarotApp 자동 백업 - %date% %time%"
git push origin master
echo 완료!
pause