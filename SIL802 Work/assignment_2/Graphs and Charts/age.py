import numpy as np
import matplotlib.pyplot as plt

#Graph 1

y = [4,2,2,4]
N = len(y)
x = range(N)
#width = 1/1.5
width = 0.35       # the width of the bars: can also be len(x) sequence
plt.bar(x, y, width, color="blue")

plt.xlabel('Age (Years)')
plt.ylabel('Number of Farmers')
plt.title('Age distribution of the farmers interviewed')

plt.ylim([0,5])

ind = np.arange(N)    # the x locations for the groups

plt.xticks(ind+width/2., ('21-30', '31-40', '41-50', '51-60') )
    
plt.show()