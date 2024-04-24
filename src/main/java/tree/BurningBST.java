package tree;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import java.io.FileWriter;

/**
 * Given a binary tree and any node that has caught fire, find out the time to burn entire tree.
 * In a unit time fire can spread to immediate children or parent.
 */
public class BurningBST {
    private class Node {
        int value;
        Node leftChild, rightChild;
        Node (int val, Node l, Node r) {
            value = val;
            leftChild = l;
            rightChild = r;
        }
        Node (int val) {
            this(val, null, null);
        }
    }

    private class NodeData {
        int height = 0;
        Integer timeToBurnMe;
        Integer maxBurnTime = 0;
    }

    FileWriter myWriter;
    Map<Integer, NodeData> nodeDataMap = new HashMap<>();

    {
        try {
            myWriter = new FileWriter("q2-test9.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        BurningBST ins = new BurningBST();
        Node root = ins.buildTree("1 2 3 N N 4 6 N 5 N N N 7");
        int n = 30;
        root = ins.createRandomBinaryTree(n);
        //root = ins.buildTree("1 N 2 3 4 5 6 N N N 7 8 9 N N N N N 10 11 N N 12 N 13 14 N N 15 N 16 17 18 N N N 19 N 20 21 N N 22 N 23 N 24 25 26 27 28 N 29 30");
        //System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(root));
        root = ins.buildTree("1 N 2 3 4 5 N 6 N 7 8 N 9 10 N 11 12 13 N N N 14 15 16 N N N N N 17 18 N 19 N 20 21 N N 22 23 24 25 N 26 N 27 28 29 N N 30 31 32 N N N N 33 34 N N 35 N 36 37 38 39 N N 40 41 N 42 43 44 45 N N 46 47 48 N N N 49 N N 50 51 N 52 53 54 55 N 56 N 57 58 59 N 60 N 61 N N 62 63 64 65 N N 66 N 67 N N 68 N N N 69 70 N N 71 72 N N 73 74 75 76 77 78 79 80 81 N N 82 83 84 N 85 N 86 87 88 89 90 N 91 N N 92 93 94 N N 95 96 97 98 99 100 101 N N 102 N N N 103 104 N N 105 106 N N 107 108 109 110 N N N N 111 N N 112 N 113 N N N 114 115 N 116 117 118 119 120 N N 121 122 N 123 N 124 125 126 N 127 128 129 130 131 132 133 134 135 136 N 137 N 138 N N 139 N 140 141 142 143 144 N 145 N N 146 147 N N N N 148 149 150 151 N 152 153 154 155 N N 156 N N 157 158 159 N 160 161 162 N 163 164 N 165 166 N N 167 N 168 N 169 N 170 N 171 172 173 174 175 176 177 N N 178 N 179 180 N N 181 182 183 N N 184 N N 185 186 187 N 188 189 190 191 192 N 193 194 195 196 197 198 N 199 200 201 202 203 204 205 206 207 N N 208 209 210 211 N N N 212 213 214 215 216 217 N N N 218 219 220 221 222 223 224 N 225 226 227 228 N 229 230 231 N N 232 N 233 234 N N N 235 236 N N 237 238 239 N 240 241 N 242 243 244 245 N N N 246 247 N 248 249 250 251 252 253 N 254 255 256 N 257 258 259 N 260 261 262 263 264 265 266 267 268 269 270 271 272 273 274 275 276 277 278 279 280 281 282 N 283 N N 284 N 285 286 287 288 289 N N 290 291 292 293 N 294 295 296 N 297 N 298 299 N 300 301 302 N N 303 304 N 305 306 307 308 309 N N 310 N 311 312 313 N 314 315 316 317 318 N N 319 320 321 322 N N 323 N 324 325 326 N 327 N 328 329 330 331 332 N 333 N N N 334 335 336 337 N 338 N 339 340 341 N 342 343 N 344 N N N 345 346 347 N 348 349 N 350 351 352 353 N N N 354 N N 355 356 N 357 358 N 359 N N 360 N 361 362 363 364 365 N 366 N N 367 N N N N 368 N N N N 369 N 370 371 N 372 N 373 374 375 376 377 378 N 379 N N 380 381 382 383 N 384 N 385 N N N N 386 N N N 387 N N 388 389 390 N 391 392 N 393 394 395 396 N 397 N 398 399 N N N 400 401 402 N N N 403 404 N N 405 N 406 N 407 N 408 N N 409 N N 410 411 N 412 413 N 414 415 N 416 N 417 N 418 N N N N 419 420 421 422 423 424 N N N 425 N 426 N 427 N N 428 N 429 430 431 432 433 434 N 435 N N 436 437 438 439 440 441 442 N 443 N N N N 444 445 446 N 447 448 449 N N N N 450 N 451 N 452 N 453 454 455 456 N 457 458 N N N N N N 459 460 461 462 463 464 465 466 467 468 469 N 470 471 N N 472 473 474 475 476 N 477 N N 478 N 479 N N 480 N N N N 481 N N 482 483 N N 484 N 485 486 487 488 N 489 N 490 491 N 492 493 N 494 N 495 N 496 N 497 N 498 N 499 N 500 501 502 N 503 N 504 505 N N N 506 N 507 508 N N 509 N 510 511 N 512 513 514 N 515 N 516 N 517 518 519 520 521 N 522 523 N N N N N 524 525 N 526 N 527 528 529 530 531 N 532 N N N 533 N N 534 N 535 N 536 537 538 539 N 540 541 N 542 543 544 545 N N N 546 547 548 549 550 551 552 553 N 554 555 556 557 N 558 559 560 561 562 N 563 N N 564 N N 565 N 566 N 567 N N 568 N N 569 570 571 N 572 N N 573 574 N 575 N 576 577 N 578 N 579 580 N N 581 582 583 N 584 N 585 N 586 587 N 588 589 N 590 N 591 592 593 594 N N N 595 596 597 598 599 N N 600 N N 601 602 603 604 605 606 N 607 608 609 610 611 N N 612 613 614 615 616 617 618 619 620 N N N N N N N 621 622 623 N 624 N 625 626 627 N 628 629 N 630 631 N N 632 633 634 635 N N N 636 637 638 N N N 639 640 641 642 N N N N 643 N N 644 N 645 646 N 647 648 649 650 651 652 653 654 655 656 N N 657 658 N 659 N 660 N 661 662 N N 663 664 665 666 N 667 N 668 N N 669 670 N 671 672 673 674 675 N N 676 N N N N 677 678 679 N N N 680 681 682 N 683 684 685 N 686 687 688 N N N 689 N N 690 N 691 N 692 693 N 694 695 N 696 N 697 N 698 N 699 700 N 701 702 703 N 704 N N 705 706 N 707 N N N 708 N 709 N 710 N 711 712 713 714 N 715 716 N 717 718 719 N 720 721 722 N N N 723 724 725 726 N 727 N N 728 729 N 730 731 732 733 734 735 N N 736 737 738 N N N N N 739 740 741 N N 742 743 N N 744 745 746 N 747 748 N N N 749 750 N N 751 752 753 754 755 N N 756 N 757 758 759 760 761 762 763 764 N 765 766 767 N N 768 769 770 771 N 772 N 773 774 N N N N N 775 776 N N N N 777 778 779 780 N N N 781 N N 782 783 N 784 785 786 787 N 788 789 N N N N 790 N N 791 792 793 N N N N 794 N 795 796 797 798 N 799 800 N 801 802 803 804 N N 805 N N 806 807 808 809 810 N 811 N 812 813 N 814 815 816 817 818 819 820 N 821 822 N N N 823 824 825 826 827 N 828 829 830 831 832 833 N 834 835 836 N N 837 838 839 840 841 N N N 842 843 N N 844 N N 845 N 846 847 N 848 N N N 849 850 851 N N N N N 852 N 853 N N 854 855 N N 856 857 N N 858 N N N 859 860 N 861 862 N N N N 863 N N 864 865 866 867 868 869 N 870 N 871 N N 872 873 874 N N 875 876 877 878 879 880 881 882 883 884 N N 885 N 886 887 888 N N 889 N N N N N 890 N 891 892 N 893 N N N 894 895 896 897 898 899 N 900 N 901 N 902 N 903 904 905 906 N 907 908 909 N N 910 911 912 N N 913 914 915 N 916 917 N 918 N 919 N 920 N 921 N 922 923 N N N N N N N N 924 N 925 N N 926 927 928 N 929 930 N N N 931 N 932 N 933 934 N N 935 936 937 N 938 939 N 940 941 942 943 944 945 N 946 N 947 N N 948 N N 949 N 950 951 N 952 N 953 954 955 N 956 957 958 959 960 961 962 963 N N N 964 965 N N N 966 967 968 969 970 971 972 N 973 N N 974 N 975 976 977 N 978 N 979 N N 980 981 982 N 983 984 N 985 N 986 N 987 988 N 989 990 N 991 N 992 993 994 995 996 N N 997 998 N 999 1000 1001 N 1002 1003 1004 1005 N 1006 N 1007 N 1008 1009 1010 N 1011 1012 1013 N 1014 N 1015 1016 1017 N 1018 1019 N 1020 N N 1021 N N 1022 1023 N 1024 1025 N N N 1026 N 1027 1028 N 1029 1030 1031 1032 1033 1034 N 1035 1036 N 1037 N 1038 1039 1040 N N N N 1041 1042 N 1043 1044 N 1045 1046 1047 N 1048 N N 1049 1050 N 1051 1052 1053 1054 N 1055 1056 1057 1058 1059 1060 N N 1061 1062 1063 1064 N 1065 1066 1067 1068 1069 1070 N N 1071 N N N N 1072 N N 1073 1074 1075 1076 N 1077 1078 1079 1080 1081 N N 1082 1083 1084 1085 N 1086 1087 N 1088 N 1089 1090 1091 1092 1093 1094 1095 1096 1097 N 1098 N N N 1099 1100 1101 N N N 1102 N 1103 1104 N N 1105 1106 N 1107 1108 1109 N N N N N N 1110 1111 1112 1113 1114 N 1115 N N N N N 1116 1117 1118 1119 1120 1121 1122 1123 1124 1125 1126 N 1127 1128 1129 N N N 1130 1131 1132 1133 1134 1135 1136 1137 N 1138 N N 1139 N 1140 N 1141 N 1142 N 1143 1144 N 1145 1146 1147 N 1148 N N N 1149 1150 N N 1151 1152 1153 N 1154 1155 1156 1157 1158 1159 1160 N 1161 N 1162 1163 N 1164 1165 1166 1167 1168 1169 1170 1171 N N 1172 1173 1174 1175 N N 1176 N 1177 1178 N N 1179 1180 N 1181 1182 1183 1184 1185 1186 1187 1188 1189 1190 1191 N 1192 1193 1194 N 1195 1196 1197 1198 N 1199 1200 N 1201 N N 1202 1203 1204 1205 1206 1207 1208 1209 N N N 1210 N 1211 1212 N 1213 1214 1215 N 1216 1217 N 1218 N N 1219 1220 1221 1222 1223 1224 1225 N N N 1226 1227 N N 1228 1229 1230 1231 1232 1233 1234 1235 N 1236 N 1237 N 1238 1239 1240 1241 1242 1243 1244 N N N 1245 1246 1247 N N 1248 1249 1250 1251 1252 1253 1254 1255 1256 1257 N N 1258 1259 N 1260 1261 1262 1263 1264 1265 N 1266 1267 N N N 1268 N N N 1269 N N 1270 1271 1272 N N N 1273 1274 1275 1276 1277 N N N 1278 N N 1279 1280 1281 1282 1283 1284 1285 1286 1287 N 1288 1289 N N N 1290 1291 1292 1293 N N N 1294 1295 N N 1296 N 1297 1298 1299 N 1300 1301 1302 1303 1304 N 1305 1306 1307 1308 1309 1310 N 1311 1312 N N 1313 1314 1315 1316 1317 1318 1319 1320 1321 1322 N 1323 N N 1324 1325 1326 1327 1328 N 1329 N 1330 N N 1331 1332 1333 1334 N N N 1335 1336 N N 1337 1338 N 1339 N N 1340 1341 N 1342 1343 1344 N N 1345 N 1346 N 1347 N 1348 N N 1349 N N 1350 1351 1352 N 1353 1354 N N 1355 N 1356 N N N 1357 1358 1359 N 1360 N 1361 1362 1363 N 1364 1365 1366 1367 1368 1369 1370 N 1371 N 1372 1373 1374 1375 1376 1377 N N 1378 1379 1380 N 1381 N N 1382 1383 1384 1385 1386 N 1387 1388 N 1389 N N N 1390 1391 N 1392 N N N 1393 N 1394 1395 1396 1397 N 1398 1399 N 1400 N 1401 N 1402 1403 1404 1405 1406 N 1407 1408 1409 1410 1411 1412 1413 1414 N 1415 1416 1417 1418 1419 1420 1421 1422 N N 1423 1424 1425 N N N N 1426 N N 1427 1428 1429 N 1430 N 1431 N 1432 N 1433 N N N 1434 N 1435 1436 N N N N 1437 1438 N N 1439 1440 1441 N 1442 1443 1444 1445 N 1446 N N 1447 1448 N N 1449 N N N N 1450 1451 1452 N N 1453 1454 1455 N 1456 N N N 1457 N N N 1458 1459 1460 N N 1461 1462 1463 1464 N 1465 1466 1467 1468 N 1469 1470 1471 1472 1473 1474 1475 1476 N 1477 1478 N N 1479 N 1480 N N 1481 1482 N N N N N 1483 N N 1484 1485 1486 1487 1488 N 1489 N N 1490 N 1491 1492 1493 N N 1494 1495 1496 1497 1498 1499 N N 1500 N 1501 N 1502 N N N 1503 N 1504 1505 N N 1506 N N N 1507 1508 1509 N 1510 N N 1511 N N 1512 1513 1514 1515 1516 N N N 1517 1518 1519 N 1520 N 1521 N N 1522 N N 1523 1524 N 1525 1526 1527 N 1528 1529 1530 N N N 1531 1532 1533 N 1534 N 1535 1536 1537 1538 1539 1540 1541 1542 1543 1544 1545 1546 1547 1548 N 1549 N 1550 1551 1552 1553 1554 1555 1556 1557 1558 1559 1560 N N N N 1561 1562 1563 1564 1565 1566 N 1567 N N N 1568 1569 1570 1571 1572 N 1573 1574 1575 1576 1577 1578 N 1579 1580 1581 1582 1583 1584 1585 1586 N N 1587 1588 1589 N N 1590 N 1591 1592 N 1593 1594 N 1595 N N 1596 1597 N 1598 N N 1599 1600 N N 1601 N 1602 1603 N 1604 N N 1605 1606 1607 1608 N 1609 N N 1610 N 1611 N 1612 1613 N 1614 N N N N N N 1615 N N N 1616 N N 1617 1618 1619 N 1620 1621 1622 1623 1624 N 1625 N N 1626 1627 N 1628 1629 1630 1631 1632 N N 1633 N N N 1634 N N 1635 1636 N N 1637 1638 N 1639 N 1640 1641 N N 1642 1643 N 1644 1645 N N 1646 N N 1647 1648 1649 1650 1651 N 1652 1653 1654 N 1655 1656 1657 1658 1659 1660 1661 N N 1662 N 1663 1664 N 1665 1666 1667 1668 N 1669 1670 1671 1672 1673 N 1674 N 1675 1676 N N 1677 N 1678 1679 N 1680 N N 1681 1682 1683 N 1684 1685 1686 1687 1688 1689 N N 1690 N N 1691 1692 N 1693 N 1694 N 1695 1696 N N 1697 1698 N 1699 N N 1700 N N N 1701 N 1702 1703 1704 1705 1706 N N 1707 N N 1708 N 1709 1710 N 1711 1712 1713 1714 1715 1716 1717 1718 1719 1720 N N 1721 N 1722 1723 N 1724 1725 1726 1727 1728 1729 1730 N 1731 1732 1733 N 1734 N N N 1735 N 1736 1737 1738 1739 1740 N 1741 1742 1743 1744 N N 1745 1746 N N 1747 1748 N N 1749 N 1750 N N N 1751 1752 N 1753 1754 N N N 1755 N 1756 N N 1757 1758 N 1759 N 1760 N 1761 N N 1762 1763 1764 1765 1766 1767 N 1768 1769 1770 1771 1772 N 1773 N N N 1774 1775 1776 N 1777 N 1778 1779 1780 N 1781 N N 1782 N N 1783 N N N 1784 1785 1786 1787 N 1788 N 1789 1790 N 1791 1792 N N N 1793 N 1794 N 1795 1796 1797 N N 1798 1799 N N 1800 1801 N 1802 1803 N 1804 1805 N N N 1806 N N N 1807 1808 N 1809 1810 N N 1811 N 1812 N N N N 1813 1814 N 1815 1816 1817 1818 1819 1820 1821 N 1822 1823 N 1824 N 1825 1826 1827 1828 1829 N 1830 1831 1832 1833 N N N N N 1834 N 1835 1836 1837 N N 1838 N N 1839 1840 1841 N 1842 N 1843 1844 1845 1846 N N 1847 1848 N N N 1849 N 1850 N 1851 N N N N 1852 N N 1853 1854 N N 1855 1856 N 1857 N 1858 1859 N N 1860 1861 N 1862 1863 N 1864 1865 N 1866 N 1867 1868 N 1869 N 1870 1871 1872 N N 1873 1874 1875 1876 N 1877 1878 N 1879 1880 N 1881 1882 N 1883 1884 N 1885 1886 1887 1888 N N 1889 1890 N 1891 1892 N N N N 1893 N 1894 N 1895 N N 1896 1897 N N 1898 N 1899 N 1900 1901 N 1902 1903 1904 1905 1906 N 1907 N 1908 1909 1910 1911 N N 1912 1913 1914 N 1915 1916 1917 1918 N 1919 1920 1921 1922 N 1923 1924 N 1925 N 1926 1927 1928 1929 1930 1931 1932 N N 1933 1934 1935 N 1936 1937 1938 N N N 1939 N N 1940 1941 N N 1942 1943 1944 N N 1945 N N N N N 1946 N N N 1947 1948 N N 1949 1950 1951 N 1952 1953 1954 1955 1956 N 1957 1958 1959 1960 N 1961 N 1962 1963 N 1964 1965 N N N 1966 1967 1968 1969 1970 1971 1972 1973 1974 N N 1975 N N 1976 N N 1977 N 1978 1979 1980 1981 1982 N 1983 N 1984 1985 1986 1987 1988 N 1989 N 1990 1991 N N N N 1992 N 1993 1994 1995 N N N 1996 1997 N 1998 N 1999 2000 N N 2001 N 2002 2003 N N N N N 2004 N 2005 2006 N 2007 N N N 2008 2009 2010 2011 N 2012 N 2013 2014 2015 2016 2017 2018 N 2019 2020 2021 N 2022 2023 N N N 2024 2025 N 2026 2027 2028 N 2029 2030 2031 2032 N 2033 N N 2034 N N 2035 2036 2037 N 2038 2039 2040 2041 N N N 2042 2043 N 2044 2045 N 2046 2047 2048 2049 N N N 2050 2051 2052 N N N N N 2053 N N N 2054 2055 2056 2057 2058 2059 2060 N N 2061 2062 N 2063 N N 2064 N N N 2065 2066 2067 2068 N 2069 2070 2071 2072 2073 N N 2074 2075 N 2076 2077 N 2078 2079 N N 2080 N N 2081 N 2082 2083 2084 N 2085 2086 2087 N N 2088 2089 2090 2091 N N N 2092 2093 2094 2095 N 2096 2097 N 2098 2099 2100 2101 2102 N 2103 2104 N N N N 2105 2106 2107 2108 N 2109 N 2110 N N 2111 N N 2112 2113 N N 2114 N 2115 2116 N N 2117 N 2118 2119 N 2120 N 2121 N N N N 2122 2123 2124 2125 2126 N N 2127 N N N N 2128 2129 N N N 2130 2131 N 2132 N 2133 N N N 2134 N 2135 2136 2137 2138 2139 N 2140 2141 N 2142 N N 2143 2144 N 2145 N 2146 N N N 2147 2148 2149 2150 2151 2152 N N N N N 2153 2154 2155 2156 2157 N N 2158 2159 2160 2161 N N 2162 2163 2164 2165 N N N 2166 2167 N 2168 2169 2170 2171 2172 N 2173 2174 2175 2176 2177 2178 N 2179 2180 2181 2182 2183 2184 2185 2186 N N 2187 2188 2189 2190 2191 2192 N 2193 N 2194 N 2195 N 2196 2197 N N N N 2198 2199 2200 2201 N 2202 N N 2203 2204 2205 2206 2207 N N 2208 2209 N 2210 N 2211 2212 N 2213 2214 2215 2216 N N N 2217 2218 N 2219 N 2220 2221 2222 N N 2223 2224 N N 2225 N N 2226 2227 N 2228 N 2229 N N 2230 2231 2232 2233 N 2234 2235 N 2236 2237 2238 N 2239 2240 N 2241 2242 N N N N 2243 2244 N 2245 2246 2247 2248 2249 2250 N 2251 2252 N 2253 N 2254 2255 N 2256 2257 N 2258 2259 2260 2261 2262 2263 N 2264 N 2265 2266 2267 2268 N 2269 2270 2271 N 2272 N 2273 2274 2275 2276 N 2277 2278 N 2279 N N N 2280 2281 2282 N 2283 N N 2284 2285 2286 2287 N N 2288 2289 2290 2291 2292 2293 2294 2295 2296 N N 2297 N 2298 N 2299 N 2300 N N N 2301 2302 2303 2304 N N 2305 N 2306 2307 2308 2309 2310 2311 2312 N N 2313 2314 2315 N 2316 2317 2318 N 2319 2320 N 2321 2322 2323 2324 2325 N N 2326 2327 N 2328 2329 N 2330 2331 N 2332 2333 N N 2334 2335 2336 N 2337 N 2338 2339 2340 2341 2342 2343 2344 2345 N 2346 2347 2348 2349 2350 2351 2352 N N 2353 2354 2355 N N N N 2356 N N N 2357 2358 2359 2360 2361 2362 2363 2364 2365 N 2366 2367 2368 2369 N 2370 2371 2372 2373 N 2374 N 2375 2376 2377 2378 2379 2380 2381 2382 2383 2384 N 2385 2386 2387 N 2388 2389 2390 N 2391 2392 2393 2394 2395 2396 2397 2398 N N 2399 2400 N 2401 2402 2403 N 2404 2405 N N N 2406 2407 N N 2408 2409 2410 2411 2412 2413 2414 2415 2416 2417 2418 N 2419 2420 N N N 2421 2422 N 2423 N 2424 N 2425 N 2426 N 2427 N N 2428 2429 2430 N 2431 N 2432 2433 2434 N N 2435 2436 N 2437 2438 N 2439 2440 2441 2442 2443 2444 2445 2446 N N 2447 N 2448 2449 2450 2451 N N N 2452 2453 N N 2454 N 2455 N N 2456 N 2457 N 2458 2459 N N 2460 2461 2462 N 2463 2464 N N N 2465 2466 2467 N 2468 2469 2470 2471 N 2472 2473 2474 2475 2476 N N 2477 2478 2479 2480 N N 2481 2482 2483 N 2484 2485 2486 N 2487 2488 2489 N 2490 2491 2492 2493 2494 2495 2496 2497 2498 2499 2500 2501 2502 N N 2503 2504 N 2505 2506 2507 2508 N 2509 2510 2511 2512 2513 N 2514 N 2515 2516 2517 2518 N 2519 N N N N 2520 N N 2521 N N 2522 2523 N 2524 2525 2526 N N 2527 N N N 2528 2529 2530 2531 2532 2533 2534 2535 2536 2537 2538 N 2539 N N 2540 2541 N N N 2542 N N 2543 2544 N 2545 2546 2547 2548 N 2549 2550 N N N N 2551 2552 N N N 2553 2554 N 2555 N 2556 2557 2558 2559 N 2560 2561 2562 2563 2564 2565 2566 2567 2568 2569 2570 2571 2572 2573 N N 2574 N N N 2575 2576 2577 N 2578 N 2579 2580 2581 N 2582 2583 N N 2584 2585 2586 2587 N 2588 N 2589 2590 2591 2592 N 2593 2594 2595 2596 2597 2598 2599 2600 2601 2602 2603 2604 N 2605 N 2606 2607 2608 N 2609 N N 2610 2611 N N N 2612 N N 2613 N 2614 2615 N 2616 2617 N 2618 N 2619 N 2620 2621 2622 N 2623 N 2624 N N N N N 2625 2626 2627 N 2628 2629 N N 2630 N 2631 2632 2633 N N 2634 2635 2636 2637 2638 N 2639 2640 2641 2642 N N N 2643 N 2644 N N N 2645 N N N N 2646 2647 2648 N 2649 N 2650 2651 2652 2653 2654 2655 N 2656 N 2657 2658 2659 N 2660 N 2661 N N 2662 N N 2663 N N N N N 2664 N N N N N 2665 N N N N N 2666 2667 2668 2669 N 2670 2671 N N 2672 2673 2674 2675 2676 N 2677 2678 N 2679 2680 N N 2681 N N N 2682 N 2683 N 2684 2685 N 2686 2687 2688 2689 2690 2691 2692 N N N 2693 2694 N 2695 N N 2696 N 2697 2698 2699 2700 2701 N N 2702 N 2703 N 2704 2705 2706 N N 2707 N N N 2708 2709 N 2710 2711 N 2712 N 2713 2714 N N 2715 N 2716 2717 N 2718 2719 N N 2720 N N 2721 N 2722 2723 N 2724 2725 N 2726 N 2727 2728 2729 N N 2730 2731 2732 2733 2734 2735 2736 N N 2737 2738 2739 N 2740 N N N 2741 2742 2743 2744 2745 2746 N 2747 N N N N N 2748 2749 N 2750 2751 2752 N N 2753 2754 2755 N N N 2756 N N 2757 2758 2759 2760 2761 N 2762 2763 N N N N N N 2764 N 2765 N 2766 2767 2768 2769 N 2770 2771 N 2772 2773 N 2774 N N N N 2775 2776 2777 2778 2779 2780 2781 2782 2783 N 2784 2785 2786 N 2787 2788 N 2789 2790 N N N 2791 N 2792 N N 2793 2794 N 2795 N 2796 2797 2798 N 2799 2800 N 2801 2802 2803 2804 2805 2806 2807 N 2808 N N N 2809 N N N 2810 2811 2812 2813 N N N 2814 2815 2816 2817 2818 N 2819 2820 N N 2821 N N N N N N N N 2822 2823 N 2824 2825 2826 2827 2828 2829 2830 2831 2832 N 2833 2834 2835 2836 2837 N 2838 N 2839 N N 2840 N N 2841 2842 2843 2844 N N N 2845 2846 N 2847 2848 N N N N 2849 2850 2851 2852 N N 2853 N 2854 2855 N 2856 2857 2858 N N 2859 N 2860 2861 2862 2863 2864 2865 2866 2867 2868 2869 2870 2871 2872 N N N N N N N 2873 N N 2874 2875 2876 N 2877 2878 N N 2879 2880 2881 2882 2883 2884 N 2885 2886 N 2887 N 2888 2889 N 2890 2891 N N 2892 2893 N 2894 2895 N 2896 2897 2898 2899 N N 2900 N 2901 2902 2903 N N N N 2904 2905 N N N 2906 N 2907 N 2908 2909 N N 2910 N 2911 N 2912 2913 2914 N 2915 2916 2917 2918 N 2919 2920 N 2921 N 2922 2923 N N 2924 2925 N 2926 N N N 2927 2928 2929 2930 2931 N 2932 2933 2934 2935 2936 2937 N N N N 2938 N 2939 N 2940 2941 N 2942 2943 2944 N N N N 2945 2946 N 2947 N N N 2948 N 2949 2950 2951 2952 N N N 2953 2954 2955 2956 N 2957 N 2958 2959 2960 2961 2962 2963 N 2964 N N N 2965 N N N 2966 2967 2968 2969 N 2970 2971 2972 N N N N 2973 2974 2975 2976 N 2977 2978 2979 N 2980 N 2981 2982 2983 N 2984 2985 2986 2987 N 2988 N N 2989 N N N 2990 2991 2992 2993 2994 2995 2996 N 2997 2998 N 2999 3000 N 3001 3002 3003 3004 N 3005 N N N 3006 3007 3008 N 3009 N 3010 3011 N 3012 3013 3014 3015 3016 N 3017 3018 3019 3020 3021 3022 N N 3023 3024 3025 3026 N 3027 3028 3029 3030 3031 N N 3032 N N 3033 3034 N N N 3035 3036 3037 3038 N N N N 3039 3040 3041 3042 3043 3044 N N 3045 3046 3047 N 3048 3049 3050 N N N 3051 3052 3053 3054 3055 N 3056 N 3057 3058 3059 3060 3061 3062 N N N 3063 3064 3065 N N 3066 3067 N N 3068 3069 3070 N 3071 N 3072 N 3073 3074 3075 3076 N N 3077 3078 N N N 3079 3080 3081 N 3082 3083 N N 3084 N N 3085 3086 3087 N N 3088 N 3089 3090 3091 N N 3092 3093 3094 N 3095 3096 N N N N 3097 3098 3099 3100 N 3101 3102 3103 3104 3105 N N 3106 3107 3108 N N N 3109 3110 3111 3112 3113 N 3114 3115 3116 3117 N 3118 3119 N 3120 3121 3122 3123 N 3124 3125 3126 3127 3128 N N N N 3129 3130 3131 N 3132 N N N 3133 N 3134 N 3135 N N 3136 3137 3138 3139 N 3140 N 3141 3142 3143 3144 3145 N 3146 3147 3148 3149 3150 N 3151 3152 N 3153 3154 3155 3156 N 3157 N 3158 N 3159 3160 N N N 3161 N 3162 3163 3164 3165 N 3166 3167 N 3168 3169 3170 3171 N N N 3172 N N 3173 3174 N N N 3175 3176 3177 3178 3179 N 3180 3181 3182 N N 3183 3184 N 3185 3186 3187 3188 3189 N N 3190 3191 3192 3193 3194 3195 3196 3197 3198 3199 3200 3201 3202 3203 3204 3205 N N 3206 N N N N N N 3207 3208 3209 N 3210 N 3211 3212 N N N 3213 3214 3215 N 3216 3217 N 3218 3219 3220 3221 3222 3223 3224 3225 3226 3227 3228 3229 3230 N N 3231 3232 N N 3233 N 3234 3235 N N 3236 3237 3238 N N 3239 3240 N 3241 N N 3242 3243 N 3244 3245 3246 3247 N 3248 N N N 3249 N N 3250 3251 N 3252 3253 3254 N N N N 3255 3256 3257 N N 3258 N 3259 N 3260 N 3261 N N N N N 3262 N N N 3263 N 3264 N 3265 N 3266 N N 3267 3268 3269 N N N 3270 3271 N 3272 3273 3274 3275 N 3276 N N N 3277 N 3278 3279 N N 3280 3281 N 3282 N N 3283 3284 3285 3286 3287 3288 3289 3290 3291 3292 N N N N N 3293 N 3294 N 3295 3296 3297 3298 3299 3300 3301 3302 N N 3303 N N N 3304 3305 3306 N N N 3307 3308 3309 N N 3310 3311 N N N 3312 N 3313 3314 3315 3316 N N 3317 N 3318 N N 3319 3320 N 3321 3322 3323 3324 N 3325 3326 3327 N 3328 N N N 3329 N 3330 3331 3332 3333 N 3334 3335 3336 N N 3337 3338 3339 3340 N N 3341 N N 3342 3343 3344 3345 N 3346 N 3347 3348 N 3349 3350 N N 3351 3352 N 3353 N N 3354 N N 3355 3356 3357 N 3358 N N 3359 3360 3361 3362 N 3363 3364 3365 N N N 3366 N N 3367 3368 3369 3370 N 3371 3372 3373 3374 N N 3375 N 3376 3377 3378 3379 N 3380 3381 3382 N 3383 3384 3385 N 3386 N 3387 3388 N 3389 3390 3391 3392 3393 N 3394 3395 3396 3397 3398 3399 3400 N 3401 3402 3403 3404 3405 3406 N 3407 3408 N 3409 3410 N 3411 N N N 3412 3413 3414 N 3415 3416 3417 N 3418 N 3419 3420 3421 3422 3423 3424 3425 3426 N N N N 3427 3428 3429 3430 3431 N 3432 N 3433 3434 N N 3435 3436 3437 3438 3439 3440 N 3441 N N 3442 N 3443 N N 3444 3445 3446 N 3447 N 3448 3449 3450 3451 3452 3453 N 3454 N 3455 N N N 3456 3457 N N 3458 3459 3460 3461 3462 3463 N 3464 3465 N N N 3466 N 3467 3468 N N 3469 3470 3471 N 3472 N 3473 N N 3474 N N 3475 3476 N 3477 N N N 3478 N 3479 3480 N N 3481 3482 N N 3483 N 3484 N 3485 3486 N 3487 N 3488 3489 3490 3491 3492 3493 3494 N N N N 3495 3496 3497 N N 3498 3499 3500 3501 N 3502 3503 3504 3505 N N N N N 3506 3507 3508 N 3509 N 3510 N N N N 3511 N N N 3512 N N N N N N 3513 N N 3514 3515 N 3516 3517 N 3518 3519 3520 3521 3522 3523 N 3524 3525 N 3526 3527 3528 N N N 3529 3530 3531 N N 3532 3533 3534 3535 3536 N 3537 N 3538 N N 3539 N N 3540 N N 3541 3542 3543 N 3544 N N 3545 N N N 3546 N N 3547 N 3548 3549 N N 3550 3551 3552 3553 3554 N 3555 3556 3557 3558 3559 N 3560 3561 N 3562 3563 N 3564 N N 3565 N N 3566 3567 3568 3569 N N 3570 3571 N N 3572 N N N N 3573 3574 3575 N 3576 N N 3577 N 3578 3579 3580 N 3581 3582 3583 3584 N 3585 N N 3586 3587 3588 N 3589 3590 3591 3592 3593 N N 3594 N 3595 3596 3597 N N 3598 3599 N 3600 N 3601 3602 N N 3603 3604 N N N 3605 3606 N 3607 3608 3609 3610 3611 N 3612 N N 3613 3614 3615 N N 3616 N N 3617 3618 N 3619 N N N 3620 3621 N 3622 N N N 3623 N N N 3624 N 3625 N 3626 N 3627 3628 N 3629 3630 3631 N 3632 3633 N 3634 N 3635 N 3636 3637 3638 3639 3640 N N N N N N 3641 N 3642 3643 N N 3644 N N N 3645 3646 3647 3648 3649 3650 3651 3652 N 3653 N N 3654 3655 3656 N 3657 N 3658 3659 3660 N N 3661 N 3662 N 3663 3664 N N 3665 3666 3667 3668 N N N N 3669 N 3670 3671 3672 N 3673 N 3674 3675 3676 N N 3677 3678 3679 3680 3681 N 3682 N 3683 3684 N 3685 3686 N 3687 N N 3688 N 3689 N 3690 3691 N 3692 N 3693 N 3694 3695 N N 3696 3697 3698 3699 3700 N 3701 3702 3703 3704 N N 3705 3706 N N 3707 N 3708 3709 N 3710 N N N 3711 3712 N 3713 3714 N N N 3715 N 3716 3717 N 3718 N 3719 3720 3721 N N 3722 3723 N 3724 3725 3726 3727 N N N 3728 3729 3730 3731 N 3732 N 3733 N N N 3734 3735 3736 3737 N 3738 N 3739 N 3740 N 3741 3742 N N N 3743 3744 N N 3745 N 3746 3747 3748 3749 N N N N 3750 3751 3752 N N 3753 N N 3754 N 3755 3756 3757 3758 N 3759 N 3760 N 3761 N 3762 3763 3764 3765 N 3766 3767 3768 N N N 3769 3770 N 3771 N 3772 3773 3774 3775 N N N 3776 3777 3778 N 3779 N 3780 3781 N 3782 N N N N 3783 3784 3785 N 3786 N N 3787 N 3788 3789 3790 3791 N N 3792 N 3793 3794 3795 N N 3796 3797 N 3798 N N 3799 3800 3801 3802 3803 N 3804 3805 N N N 3806 3807 3808 N 3809 N 3810 N 3811 3812 N 3813 3814 3815 3816 3817 3818 N 3819 3820 N N 3821 N 3822 N N 3823 3824 3825 3826 3827 N 3828 3829 N 3830 3831 N N N 3832 N N N 3833 N N N 3834 3835 N 3836 3837 3838 3839 3840 N 3841 N 3842 3843 3844 3845 3846 N N 3847 3848 3849 3850 N 3851 3852 N N 3853 3854 3855 N N N 3856 N 3857 N N 3858 N N 3859 3860 N N 3861 3862 3863 3864 3865 N 3866 3867 N 3868 N 3869 3870 N N N 3871 3872 N N 3873 3874 3875 3876 3877 3878 N N 3879 N 3880 N N N N 3881 3882 3883 3884 3885 N 3886 3887 3888 N 3889 3890 3891 3892 N N 3893 3894 N 3895 3896 3897 3898 N 3899 N 3900 3901 3902 N N N 3903 3904 N N 3905 N N 3906 3907 3908 N 3909 3910 3911 N 3912 3913 N 3914 3915 N N N 3916 N 3917 3918 N 3919 N N N 3920 N N N 3921 3922 3923 N 3924 N 3925 3926 N 3927 N N N 3928 3929 3930 3931 3932 3933 3934 3935 3936 3937 3938 3939 3940 N 3941 N 3942 3943 N 3944 3945 3946 N 3947 3948 3949 3950 3951 N 3952 N 3953 N 3954 3955 N 3956 3957 N N 3958 3959 N N N N N N 3960 N N N N N 3961 N 3962 3963 N 3964 3965 N 3966 3967 3968 3969 3970 3971 N N 3972 N 3973 3974 3975 3976 N 3977 3978 3979 3980 3981 3982 3983 3984 3985 N 3986 3987 3988 3989 N N 3990 3991 3992 3993 N N 3994 3995 3996 N 3997 3998 3999 4000 N 4001 4002 N 4003 4004 4005 N 4006 4007 N N N 4008 N N N 4009 4010 4011 N 4012 4013 4014 4015 N 4016 4017 N N 4018 4019 N 4020 N N 4021 4022 N N N 4023 4024 N 4025 N 4026 4027 4028 4029 4030 4031 4032 4033 4034 4035 N N N 4036 N N 4037 N 4038 4039 4040 4041 4042 N 4043 N N 4044 N 4045 4046 4047 4048 4049 4050 4051 N 4052 4053 N N 4054 4055 N N N 4056 N 4057 N 4058 N N 4059 4060 4061 N N N N 4062 N 4063 4064 4065 4066 4067 N N N N 4068 N 4069 4070 N 4071 N 4072 4073 4074 N 4075 4076 N N N N 4077 4078 N N 4079 4080 4081 4082 4083 N N 4084 N N N 4085 N 4086 4087 N N 4088 N 4089 N N 4090 4091 N N N 4092 N 4093 N 4094 N 4095 4096 4097 N 4098 N 4099 4100 N 4101 4102 4103 N 4104 N N N 4105 4106 N N N N N 4107 4108 4109 4110 4111 4112 4113 N N N N 4114 N N N 4115 N 4116 4117 4118 4119 N N N 4120 4121 N 4122 4123 4124 4125 4126 N 4127 4128 N 4129 4130 4131 4132 4133 N N 4134 4135 4136 N 4137 4138 4139 4140 4141 4142 4143 N N 4144 N 4145 4146 4147 4148 4149 4150 4151 N 4152 N N N N N 4153 4154 N 4155 4156 N 4157 4158 N 4159 4160 4161 N 4162 N N 4163 4164 N 4165 4166 N 4167 N 4168 4169 4170 N 4171 N 4172 N 4173 N 4174 4175 N 4176 N 4177 4178 N N 4179 N N 4180 4181 4182 4183 N N 4184 4185 4186 4187 4188 4189 4190 N N 4191 4192 4193 4194 4195 4196 4197 4198 N N 4199 4200 4201 4202 4203 4204 N 4205 4206 N N 4207 N N 4208 4209 4210 4211 4212 4213 4214 N N N 4215 4216 N N 4217 4218 4219 N 4220 4221 4222 4223 4224 N 4225 4226 4227 4228 N 4229 4230 4231 N N 4232 4233 N N N N 4234 4235 4236 4237 N N 4238 4239 4240 N N 4241 4242 N N 4243 N 4244 4245 4246 4247 N N 4248 4249 N 4250 N 4251 4252 N 4253 N 4254 4255 4256 4257 4258 N N 4259 N N 4260 N 4261 N 4262 4263 N 4264 4265 4266 4267 4268 4269 4270 N 4271 N N 4272 4273 4274 4275 4276 N 4277 4278 4279 4280 N 4281 4282 4283 N N 4284 4285 4286 4287 N 4288 4289 4290 4291 4292 4293 4294 4295 N 4296 N 4297 4298 N N N N 4299 4300 4301 4302 4303 4304 4305 4306 4307 N N N 4308 4309 4310 4311 4312 N 4313 N N 4314 4315 4316 4317 N 4318 4319 4320 4321 4322 4323 N N 4324 N 4325 4326 4327 N 4328 4329 N 4330 4331 N N 4332 4333 4334 4335 4336 4337 4338 N N N N 4339 4340 N 4341 4342 4343 N N N N 4344 N N N 4345 N N N N N 4346 N N N 4347 4348 4349 4350 N N N N 4351 4352 4353 N N N 4354 N 4355 4356 4357 N 4358 4359 4360 4361 4362 N N 4363 4364 N N N N 4365 4366 4367 4368 N N 4369 4370 N 4371 N N N 4372 N 4373 4374 4375 N 4376 4377 N N 4378 N 4379 N N N N 4380 N 4381 N N 4382 4383 N 4384 4385 N N N 4386 N 4387 4388 N N 4389 N 4390 N N 4391 4392 N 4393 N N 4394 N N 4395 N N 4396 4397 4398 4399 N 4400 4401 N N N N N 4402 N N N 4403 4404 4405 4406 4407 4408 N N N 4409 4410 4411 N 4412 4413 N 4414 N 4415 4416 4417 4418 4419 4420 N 4421 4422 N N 4423 N 4424 N N N N N 4425 4426 N N 4427 N 4428 4429 4430 4431 N 4432 N 4433 4434 N 4435 N 4436 4437 4438 4439 4440 N 4441 N 4442 4443 4444 4445 4446 N N N N 4447 N 4448 N 4449 N N N N 4450 N 4451 N 4452 N N 4453 N N 4454 4455 4456 4457 N 4458 N N 4459 4460 4461 N 4462 N N 4463 4464 4465 N 4466 4467 4468 N 4469 4470 N N 4471 N 4472 4473 4474 N 4475 4476 4477 N 4478 4479 4480 4481 N N N N N 4482 4483 4484 4485 N 4486 4487 N 4488 4489 N 4490 4491 N 4492 4493 4494 N 4495 N N 4496 N 4497 N 4498 N 4499 4500 N 4501 4502 4503 N N 4504 N N 4505 N 4506 4507 4508 4509 4510 N 4511 4512 N 4513 N N N N 4514 N 4515 N N N 4516 N N 4517 4518 4519 4520 N 4521 N 4522 4523 4524 4525 N 4526 4527 4528 N 4529 4530 4531 4532 4533 4534 4535 N N N N 4536 4537 4538 4539 4540 N 4541 N N 4542 4543 N N N N 4544 N N N N N 4545 N N 4546 N N 4547 4548 N 4549 4550 4551 N 4552 4553 N N N 4554 4555 N 4556 4557 4558 4559 4560 4561 4562 4563 N N 4564 4565 4566 N N 4567 N 4568 N 4569 4570 4571 N N 4572 N 4573 4574 4575 4576 N N N 4577 4578 N N 4579 4580 4581 4582 4583 4584 4585 N N 4586 4587 N 4588 N N 4589 N 4590 4591 4592 4593 N N N N 4594 N N 4595 4596 N 4597 4598 4599 N 4600 4601 4602 4603 4604 4605 4606 4607 4608 4609 4610 4611 4612 N 4613 4614 N N N N 4615 N N N 4616 4617 4618 4619 N 4620 4621 4622 N 4623 4624 N 4625 N 4626 4627 4628 N N 4629 4630 N 4631 N N N N N 4632 4633 4634 N 4635 N N 4636 4637 4638 N 4639 4640 4641 N N N 4642 4643 4644 4645 N 4646 4647 4648 4649 4650 N 4651 N 4652 N N 4653 N N 4654 4655 N N 4656 N N 4657 4658 4659 4660 4661 N N N 4662 4663 4664 4665 N N 4666 4667 4668 4669 N N 4670 4671 N 4672 N 4673 N 4674 4675 N N 4676 N 4677 4678 4679 4680 4681 N N N 4682 N 4683 4684 4685 4686 4687 4688 4689 4690 4691 N 4692 4693 N N 4694 N 4695 N 4696 N N 4697 4698 N N N 4699 4700 N 4701 4702 4703 4704 4705 4706 4707 4708 4709 4710 N N 4711 4712 4713 4714 N N 4715 N 4716 4717 4718 4719 N 4720 N 4721 4722 N N 4723 4724 4725 4726 N 4727 4728 N 4729 4730 4731 N 4732 4733 N 4734 N N 4735 4736 4737 4738 4739 N N N N 4740 N N 4741 N N 4742 4743 N 4744 N 4745 4746 N 4747 N 4748 N N N N 4749 N 4750 4751 4752 4753 4754 4755 N 4756 4757 N 4758 N 4759 N N N N 4760 4761 N N 4762 N 4763 N 4764 4765 4766 4767 4768 4769 4770 4771 N 4772 4773 N N N 4774 4775 4776 4777 4778 4779 4780 N 4781 4782 N 4783 4784 4785 4786 N N N 4787 4788 4789 4790 4791 N 4792 N 4793 4794 N 4795 4796 4797 4798 N 4799 4800 N N 4801 4802 N N 4803 N 4804 4805 N N N N 4806 N 4807 N N 4808 4809 N 4810 N N 4811 4812 N 4813 4814 N N N 4815 N 4816 N 4817 4818 N 4819 4820 N N 4821 N 4822 4823 4824 N N N 4825 N N 4826 N 4827 N 4828 N 4829 N 4830 N 4831 N 4832 4833 N 4834 4835 4836 N 4837 4838 4839 4840 4841 N 4842 4843 4844 4845 4846 N 4847 4848 4849 N 4850 4851 N N 4852 N 4853 4854 4855 N 4856 N 4857 4858 4859 N 4860 4861 N 4862 N N 4863 4864 N 4865 N 4866 4867 4868 4869 4870 4871 4872 4873 N N N N 4874 N 4875 4876 N 4877 4878 4879 N 4880 4881 N 4882 4883 4884 4885 4886 4887 N 4888 N 4889 4890 N N N 4891 N 4892 4893 N 4894 N N N 4895 N 4896 N N 4897 4898 4899 4900 4901 N 4902 4903 N 4904 N N N N 4905 N 4906 4907 4908 4909 N 4910 4911 N 4912 N N 4913 4914 4915 4916 4917 4918 N 4919 4920 N N 4921 N 4922 4923 4924 N 4925 N N N 4926 4927 4928 4929 N 4930 4931 4932 4933 4934 4935 N N 4936 4937 N 4938 4939 4940 N 4941 4942 N 4943 4944 N 4945 4946 N 4947 4948 N 4949 4950 4951 4952 N 4953 4954 4955 4956 N N N 4957 N 4958 N 4959 4960 4961 4962 4963 N N N N 4964 4965 4966 4967 4968 4969 N N 4970 4971 4972 4973 N N 4974 N N 4975 N 4976 4977 N N 4978 4979 4980 N 4981 4982 N 4983 4984 4985 N 4986 4987 N N 4988 N N N N N 4989 4990 N 4991 4992 N 4993 4994 N N 4995 N N N 4996 4997 4998 4999 5000");

        int fireNode = 4367;
        //fireNode = new Random().nextInt(n) + 1;
        try {
            ins.myWriter.write("\n" + fireNode);
            ins.myWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        NodeData nodeData = ins.spreadFire(root, fireNode, null);
        System.out.println("Time required to burn down whole tree when starting from Node : "
                + fireNode + " is : " + nodeData.maxBurnTime);
        try {
            ins.myWriter = new FileWriter("q2-test9_soln.txt");
            ins.myWriter.write((ins.nodeDataMap.get(fireNode).height-1) + "\n" + nodeData.maxBurnTime);
            ins.myWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Spread fire from node to node while calculating NodeData (DFS using recursion)
    // Time Complexity = O(number of nodes)
    private NodeData spreadFire(Node node, int fireNode, Integer timeToBurnParent) {
        NodeData nd = new NodeData();
        if (node != null) {
            if (node.value == fireNode) {
                nd.timeToBurnMe = 0;
            } else {
                nd.timeToBurnMe = timeToBurnParent == null ? null : timeToBurnParent + 1;
            }
            NodeData nd1 = spreadFire(node.leftChild, fireNode, nd.timeToBurnMe);
            NodeData nd2 = spreadFire(node.rightChild, fireNode, nd.timeToBurnMe);
            nd.height = Math.max(nd1.height, nd2.height) + 1;
            if (nd.timeToBurnMe == null && nd1.timeToBurnMe != null) {
                // Left child on fire Situation
                nd.timeToBurnMe = nd1.timeToBurnMe + 1;
                nd.maxBurnTime = Math.max(nd1.maxBurnTime != null ? nd1.maxBurnTime : 0, nd.timeToBurnMe + nd2.height);
            } else if (nd.timeToBurnMe == null && nd2.timeToBurnMe != null) {
                // Right child on fire Situation
                nd.timeToBurnMe = nd2.timeToBurnMe + 1;
                nd.maxBurnTime = Math.max(nd2.maxBurnTime != null ? nd2.maxBurnTime : 0, nd.timeToBurnMe + nd1.height);
            } else {
                nd.maxBurnTime = Math.max(nd.timeToBurnMe != null ? nd.timeToBurnMe : 0, Math.max(nd1.maxBurnTime, nd2.maxBurnTime));
            }
            nodeDataMap.put(node.value, nd);
            System.out.println("Node - " + node.value);
            System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(nd));
        }
        return nd;
    }

    // Build tree from String like "1 2 3 N N 4 6 N 5 N N N 7"
    private Node buildTree(String inputLine) {
        Scanner sc = new Scanner(new StringReader(inputLine));
        Queue<Integer> que = new LinkedList<>();
        Map<Integer, Node> nodeMap = new HashMap<>();
        Node root = null;
        for (int counter = 0; sc.hasNext(); counter++) {
            String nextInp = sc.next();
            Integer parent = que.peek();
            if (counter % 2 == 0)
                que.poll();
            if (nextInp.equalsIgnoreCase("N"))
                continue;
            int nextInt = Integer.parseInt(nextInp);
            que.add(nextInt);

            Node newNode = new Node(nextInt);
            nodeMap.put(nextInt, newNode);
            if (parent == null) {
                root = nodeMap.get(nextInt);
            } else {
                if (counter % 2 == 1)
                    nodeMap.get(parent).leftChild = newNode;
                else
                    nodeMap.get(parent).rightChild = newNode;
            }
        }
        sc.close();
        return root;
    }

    // Create a somewhat random binary tree with N nodes
    private Node createRandomBinaryTree(int nodes) {
        StringBuffer sb = new StringBuffer("");
        Random r = new Random();
        int i = 1;
        int allowedN = 1;
        sb.append(i++);
        while (i <= nodes) {
            sb.append(' ');
            if (allowedN > 0 && r.nextFloat() < 0.4) {
                sb.append('N');
                allowedN--;
            } else {
                sb.append(i++);
                allowedN++;
            }
        }
        System.out.println(sb.toString());
        try {
            myWriter.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return buildTree(sb.toString());
    }
}
