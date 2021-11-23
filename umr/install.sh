#mvn clean dependency:copy-dependencies package
#wget -cO -  $(curl -s https://api.github.com/repos/checkstyle/checkstyle/releases/latest | grep browser_download_url | cut -d '"' -f 4) > checkstyle.jar
#chmod +x checkstyle.jar