# MEGA Pairwise Distance Matrix2Column converter

This project provides a simple parser that reads MEGA CC output, extracts the 
list of species and its distances matrix, and outputs the distances as a 
pairwise column.

e.g.: The matrix below:

(...) # the parser ignores the rest of the file

[ 1] #arthuri_MG33_13_7
[ 2] #strodeiES09_1
[ 3] #strodeiES09_3
[ 4] #strodeiVP06_6_4

[      1      2      3      4      ]
[ 1]
[ 2]  0.04039287
[ 3]  0.03866546 0.01639325
[ 4]  0.03871664 0.01475963 0.00813598

Would output: 

<table>
<tr>
<th>Species 1</th><th>Species 2</th><th>Dist</th>
</tr>
<tr>
<td>arthuri_MG33_13_7</td><td>strodeiES09_1</td><td>0.04039287</td>
</tr>
<tr>
<td>arthuri_MG33_13_7</td><td>strodeiES09_3</td><td>0.03866546</td>
</tr>
<tr>
<td>strodeiES09_1</td><td>strodeiES09_3</td><td>0.01639325</td>
</tr>
<tr>
<td>arthuri_MG33_13_7</td><td>strodeiVP06_6_4</td><td>0.03871664</td>
</tr>
<tr>
<td>strodeiES09_1</td><td>strodeiVP06_6_4</td><td>0.01475963</td>
<tr/>
<tr>
<td>strodeiES09_3</td><td>strodeiVP06_6_4</td><td>0.00813598</td>
</tr>
</table>

