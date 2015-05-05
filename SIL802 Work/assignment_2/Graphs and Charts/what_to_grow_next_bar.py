import numpy as np
import matplotlib.pyplot as plt

#Graph 1

y = [10,1,1]
N = len(y)
x = range(N)
#width = 1/1.5
width = 0.35       # the width of the bars: can also be len(x) sequence
plt.bar(x, y, width, color="blue")

plt.xlabel('Method')
plt.ylabel('Number of Farmers')
plt.title('How do you decide what to grow next?')

plt.ylim([0,11])

ind = np.arange(N)    # the x locations for the groups

plt.xticks(ind+width/2., ('ancestral crop','Depending on price','Crop rotation (good for land)' ) )
    
plt.show()