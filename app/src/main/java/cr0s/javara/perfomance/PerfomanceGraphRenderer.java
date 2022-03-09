package cr0s.javara.perfomance;

import java.util.Map.Entry;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.fonts.*;
import android.graphics.Color;

import cr0s.javara.main.Main;
import cr0s.javara.util.Pos;

public class PerfomanceGraphRenderer {
    public final static int WIDTH = 300;
    public final static int HEIGHT = 150;
    
    private static final Pos basis = new Pos(WIDTH / 100, HEIGHT / 100);
    private static final Color BORDER_COLOR = Color.valueOf(128, 128, 128, 128);
    private static final Color BG_COLOR = Color.valueOf(0, 0, 0, 200);
    
    //static TrueTypeFont SMALL_FONT = new TrueTypeFont(new Font.Builder() , true);
    
    public static void render(Canvas g, Pos pos)
	{
		g.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
		g.drawRect(pos.getX(), pos.getY(), WIDTH + 2, HEIGHT + 2);

		//g.setLineWidth(2);

		g.drawColor(BORDER_COLOR);
		g.drawRect(pos.getX(), pos.getY(), WIDTH + 2, HEIGHT + 2);

		// Draw perfomance graphs
		int k = 0;
		float drawX = pos.getX();
		float drawY = pos.getY() + HEIGHT - 2;

		//g.setLineWidth(1);
		for (Entry<String, ProfilingSection> item : Profiler.getInstance().profilers.entrySet()) {
			ProfilingSection s = item.getValue();
			String name = item.getKey();

			g.drawColor(s.color);

			// Draw graph for current section
			Integer previousValue = 0;
			int n = 0; // number of sample
			for (Integer currentValue : s.history) {
				float previousX = drawX + n * basis.getX();
				float previousY = drawY - previousValue * basis.getY();

				float currentX = drawX + (n + 1) * basis.getX();
				float currentY = drawY - currentValue * basis.getY();

				g.drawLine(previousX, previousY, currentX, currentY, new Paint());

				previousValue = currentValue;

				n++;
			}

			String info = name + ": " + String.format("%.1f", s.getAvgMsPassed()) + " ms.";

			g.setFont(SMALL_FONT);
			int w = g.getFont().getWidth(info);
			int h = g.getFont().getHeight(info);

			g.drawText(info, drawX + WIDTH - w - 4, pos.getY() + (h + 4) * k);

			k++;
		}
    }
}
