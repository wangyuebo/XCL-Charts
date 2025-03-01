/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version v0.1
 */

package org.xclcharts.chart;

import java.util.List;

import org.xclcharts.chart.common.DrawHelper;
import org.xclcharts.chart.common.IFormatterDoubleCallBack;
import org.xclcharts.renderer.AxisChart;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.bar.Bar;
import org.xclcharts.renderer.bar.FlatBar;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;

/**
 * @ClassName BarChart
 * @Description  柱形图的基类,包含横向和竖向柱形图
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  * MODIFIED    YYYY-MM-DD   REASON
 */

public class BarChart extends AxisChart {

	// 柱形基类
	private FlatBar mFlatBar = new FlatBar();
	// 格式化柱形上的标签
	private IFormatterDoubleCallBack mItemLabelFormatter;
	// 数据源
	private List<BarData> mDataSet;
	// 确定是竖向柱形图(默认)还是横向
	private XEnum.Direction mDirection = XEnum.Direction.VERTICAL;

	public BarChart() {
		super();
		// 默认显示Key
		setPlotKeyVisible(true);
		
		//默认为竖向设置
		defaultAxisSetting();	
	}

	/**
	 * 开放柱形绘制类
	 * @return 柱形绘制类
	 */
	public Bar getBar() {
		return mFlatBar;
	}

	/**
	 * 设置柱形顶上标签显示格式
	 * @param callBack 回调函数
	 */
	public void setItemLabelFormatter(IFormatterDoubleCallBack callBack) {
		this.mItemLabelFormatter = callBack;
	}

	/**
	 * 得到柱形顶上标签显示值
	 * @param value 值
	 * @return 转换后的文本标签
	 */
	protected String getFormatterItemLabel(double value) {
		String itemLabel = "";
		try {
			itemLabel = mItemLabelFormatter.doubleFormatter(value);
		} catch (Exception ex) {
			itemLabel = Double.toString(value);
		}
		return itemLabel;
	}

	/**
	 * 标签轴的数据源
	 * 
	 * @param labels
	 *            标签集
	 */
	public void setLabels(List<String> labels) {
		labelsAxis.setDataBuilding(labels);
	}

	/**
	 * 设置数据轴的数据源
	 * 
	 * @param dataSeries
	 *            数据源
	 */
	public void setDataSource(List<BarData> dataSeries) {
		this.mDataSet = dataSeries;
	}

	public List<BarData> getDataSource() {
		return mDataSet;
	}

	/**
	 * 设置图的显示方式,即横向还是竖向显示柱形
	 * @param direction 横向/竖向
	 */
	public void setChartDirection(XEnum.Direction direction) {
		mDirection = direction;
		
		defaultAxisSetting();		
	}
	
	/**
	 * 返回图的显示方式,即横向还是竖向显示柱形
	 * @return 横向/竖向
	 */
	public XEnum.Direction getChartDirection()
	{
		return mDirection;
	}
	
	/**
	 * 图为横向或竖向时，轴和Bar的默认显示风格
	 */
	protected void defaultAxisSetting()
	{
		try{
			switch (mDirection) {
				case HORIZONTAL: {
					labelsAxis.setAxisHorizontalTickAlign(Align.LEFT);		
					labelsAxis.getAxisTickLabelsPaint().setTextAlign(Align.RIGHT);					
					dataAxis.setAxisHorizontalTickAlign(Align.CENTER);
					dataAxis.getAxisTickLabelsPaint().setTextAlign(Align.CENTER);					
					getBar().getItemLabelsPaint().setTextAlign(Align.LEFT);			
					break;
				}
				case VERTICAL: {					
					dataAxis.setAxisHorizontalTickAlign(Align.LEFT);
					dataAxis.getAxisTickLabelsPaint().setTextAlign(Align.RIGHT);					
									
					labelsAxis.setAxisHorizontalTickAlign(Align.CENTER);			
					labelsAxis.getAxisTickLabelsPaint().setTextAlign(Align.CENTER);					
					labelsAxis.setAxisVerticalTickPostion(XEnum.Postion.LOWER);				
					break;
				}
			}
		}catch(Exception ex){
			Log.e("ERROR-BarChart", ex.toString());
		}
	}


	/**
	 * 绘制柱形键值对应的说明描述
	 * 
	 * @param barWidth
	 *            柱形宽度
	 * @param lableOffsetHeight
	 *            柱形宽度
	 */
	protected void drawDataSetKey() {
		if (false == this.getPlotKeyVisible())
			return;

		// 图表标题显示位置
		switch (this.getPlotTitle().getChartTitleAlign()) {
		case CENTER:
		case RIGHT:
			drawDataSetKeyLeft();
			break;
		case LEFT:
			drawDataSetKeyRight();
			break;
		}
	}

	/**
	 * 单行可以显示多个Key说明，当一行显示不下时，会自动转到新行
	 */
	private void drawDataSetKeyLeft() {

		DrawHelper dw = new DrawHelper();

		float keyTextHeight = dw.getPaintFontHeight(this
				.getPlotDataSetKeyPaint());
		float keyLablesX = this.plotArea.getPlotLeft();
		float keyLablesY = this.plotArea.getPlotTop() - keyTextHeight;

		// 宽度是个小约定，两倍文字高度即可
		float rectWidth = 2 * keyTextHeight;
		float rectHeight = keyTextHeight;
		float rectOffset = getPlotDataSetKeyMargin();
		
		getPlotDataSetKeyPaint().setTextAlign(Align.LEFT);
		for (BarData cData : mDataSet) {
			String key = cData.getKey();
			getPlotDataSetKeyPaint().setColor(cData.getColor());
			float strWidth = getPlotDataSetKeyPaint().measureText(key, 0,
					key.length());

			if (keyLablesX + 2 * rectWidth + strWidth > this.getChartRight()) {
				keyLablesX = this.plotArea.getPlotLeft();
				keyLablesY = keyLablesY + rectHeight * 2;
			}

			mCanvas.drawRect(keyLablesX, keyLablesY, keyLablesX + rectWidth,
					keyLablesY - rectHeight, getPlotDataSetKeyPaint());

			getPlotDataSetKeyPaint().setTextAlign(Align.LEFT);
			dw.drawRotateText(key, keyLablesX + rectWidth + rectOffset,
					keyLablesY, 0, this.mCanvas, getPlotDataSetKeyPaint());

			keyLablesX += rectWidth + strWidth + 2 * rectOffset;
		}

	}

	/**
	 * 显示在右边时，采用单条说明占一行的方式显示
	 */
	private void drawDataSetKeyRight() {
		if (false == getPlotKeyVisible())
			return;

		DrawHelper dw = new DrawHelper();

		float keyTextHeight = dw.getPaintFontHeight(getPlotDataSetKeyPaint());
		float keyLablesX = this.plotArea.getPlotRight();
		float keyLablesY = (float) (this.getChartTop() + keyTextHeight);

		// 宽度是个小约定，两倍文字高度即可
		float rectWidth = 2 * keyTextHeight;
		float rectHeight = keyTextHeight;
		float rectOffset = getPlotDataSetKeyMargin();

		getPlotDataSetKeyPaint().setTextAlign(Align.RIGHT);
		for (BarData cData : mDataSet) {
			String key = cData.getKey();
			getPlotDataSetKeyPaint().setColor(cData.getColor());

			mCanvas.drawRect(keyLablesX, keyLablesY, keyLablesX - rectWidth,
					keyLablesY + rectHeight, getPlotDataSetKeyPaint());

			dw.drawRotateText(key, keyLablesX - rectWidth - rectOffset,
					keyLablesY + rectHeight, 0, this.mCanvas,
					getPlotDataSetKeyPaint());

			keyLablesY += keyTextHeight;
		}

	}

	/**
	 * 比较传入的各个数据集，找出最大数据个数
	 * @return 最大数据个数
	 */
	protected int getDataAxisDetailSetMaxSize() {
		// 得到最大size个数
		int dsetMaxSize = 0;
		for (int i = 0; i < mDataSet.size(); i++) {
			if (dsetMaxSize < mDataSet.get(i).getDataSet().size())
				dsetMaxSize = mDataSet.get(i).getDataSet().size();
		}
		return dsetMaxSize;
	}

	/**
	 * 	竖向柱形图
	 *  Y轴的屏幕高度/数据轴的刻度标记总数 = 步长
	 * @return Y轴步长
	 */
	private float getVerticalYSteps(double tickCount) {
		//return (float) Math.floor(getAxisScreenHeight() / tickCount);
		
		return (float)(getAxisScreenHeight() / tickCount);
	}

	/**
	 * 竖向柱形图
	 * 得到X轴的步长
	 * X轴的屏幕宽度 / 刻度标记总数  = 步长
	 * @param num 刻度标记总数 
	 * @return X轴步长
	 */
	protected float getVerticalXSteps(int num) {
		//柱形图为了让柱形显示在tick的中间，会多出一个步长即(dataSet.size()+1)
		float XSteps = (float) Math.ceil(getAxisScreenWidth() / num);
		return XSteps;
	}

	
	/**
	 * 横向柱形图,Y轴显示标签
	 * Y轴的屏幕高度/(标签轴的刻度标记总数+1) = 步长
	 * @return Y轴步长
	 */
	protected float getHorizontalYSteps() {
		
		float YSteps = (float) Math.ceil((getAxisScreenHeight())
				/ (this.labelsAxis.getDataSet().size() + 1));
		return YSteps;
	}

	/**
	 * 绘制左边竖轴，及上面的刻度线和标签
	 */
	protected void renderVerticalBarDataAxis() {
		// 数据轴数据刻度总个数
		double tickCount = dataAxis.getAixTickCount();
		// 数据轴高度步长
		float YSteps = getVerticalYSteps(tickCount);
		float currentY = plotArea.getPlotBottom();
		float maskHeight =  dataAxis.getAxisTickMarksPaint().getStrokeWidth()/2;

		// 数据轴(Y 轴)
		for (int i = 0; i <= tickCount; i++) {
			if (i == 0)
				continue;
			// 依起始数据坐标与数据刻度间距算出上移高度
			//currentY = (float) Math.rint(plotArea.getPlotBottom() - i * YSteps);
			currentY = (float)(plotArea.getPlotBottom() - i * YSteps);
			// 标签
			float currentTickLabel = (float) (dataAxis.getAxisMin() + (i * dataAxis
					.getAxisSteps()));
						
			// 从左到右的横向网格线		
			if ( i % 2 != 0) {
				plotGrid.renderOddRowsFill(plotArea.getPlotLeft(), currentY
						+ YSteps, plotArea.getPlotRight(), currentY);
			} else {
				plotGrid.renderEvenRowsFill(plotArea.getPlotLeft(), currentY
						+ YSteps, plotArea.getPlotRight(), currentY);
			}
			
			plotGrid.renderGridLinesHorizontal(plotArea.getPlotLeft(),
					currentY, plotArea.getPlotRight(), currentY);
			
					
			if(i == tickCount)
			{
				dataAxis.renderAxisHorizontalTick(plotArea.getPlotLeft(),
												  plotArea.getPlotTop() , //- maskHeight, 
								Float.toString(currentTickLabel));
			}else{
				this.dataAxis.renderAxisHorizontalTick(plotArea.getPlotLeft(),
						currentY, Float.toString(currentTickLabel));
			}
			
		}
		// 轴 线
		dataAxis.renderAxis(plotArea.getPlotLeft(), plotArea.getPlotBottom(),
				plotArea.getPlotLeft(), plotArea.getPlotTop());
	}

	/**
	 * 绘制竖向柱形图中的底部标签轴
	 */
	protected void renderVerticalBarLabelsAxis() {
		// 标签轴(X 轴)
		float currentX = plotArea.getPlotLeft();

		// 得到标签轴数据集
		List<String> dataSet = labelsAxis.getDataSet();

		// 依传入的标签个数与轴总宽度算出要画的标签间距数是多少
		// 总宽度 / 标签个数 = 间距长度
		int XSteps = (int) Math.ceil(getAxisScreenWidth()
				/ (dataSet.size() + 1));

		for (int i = 0; i < dataSet.size(); i++) {
			// 依初超始X坐标与标签间距算出当前刻度的X坐标
			currentX = Math.round(plotArea.getPlotLeft() + (i + 1) * XSteps);

			// 绘制横向网格线
			if (plotGrid.getVerticalLinesVisible()) {
				this.mCanvas.drawLine(currentX, plotArea.getPlotBottom(),
						currentX, plotArea.getPlotTop(),
						this.plotGrid.getVerticalLinesPaint());
			}
			// 画上标签/刻度线
			labelsAxis.renderAxisVerticalTick(currentX,
					plotArea.getPlotBottom(), dataSet.get(i));
		}
	}

	/**
	 * 绘制横向柱形图中的数据轴
	 */
	protected void renderHorizontalBarDataAxis() {
		// 依数据轴最大刻度值与数据间的间距 算出要画多少个数据刻度
		double tickCount = dataAxis.getAixTickCount();		
		// 得到数据标签刻度间距
		float XSteps = (float) Math.ceil(this.getAxisScreenWidth() / tickCount);

		// x 轴
		float currentX = this.plotArea.getPlotLeft();
		for (int i = 0; i <= tickCount; i++) {	
			
			if (i == 0)continue;
			
			// 依起始数据坐标与数据刻度间距算出上移高度
			currentX = (int) Math.round((this.plotArea.getPlotLeft() + i
					* XSteps));
									
			float currentTickLabel = (float) (dataAxis.getAxisMin() + (i * dataAxis
					.getAxisSteps()));
			this.dataAxis.renderAxisVerticalTick(currentX,
					plotArea.getPlotBottom(), Float.toString(currentTickLabel));
		

			// 从底到上的竖向网格线
			if (i % 2 != 0) {
				plotGrid.renderOddRowsFill(currentX, plotArea.getPlotTop(),
						currentX - XSteps, plotArea.getPlotBottom());
			} else {
				plotGrid.renderEvenRowsFill(currentX, plotArea.getPlotTop(),
						currentX - XSteps, plotArea.getPlotBottom());
			}
			
			plotGrid.renderGridLinesVertical(currentX,
					plotArea.getPlotBottom(), currentX, plotArea.getPlotTop());
		}
	}

	/**
	 * 绘制横向柱形图中的标签轴
	 */
	protected void renderHorizontalBarLabelAxis() {
		// Y 轴
		// 标签横向间距高度
		float YSteps = (float) Math.ceil(this.getAxisScreenHeight()
				/ (this.labelsAxis.getDataSet().size() + 1));
		float currentY = 0.0f;
		for (int i = 0; i < labelsAxis.getDataSet().size(); i++) {
			// 依初超始Y坐标与标签间距算出当前刻度的Y坐标
			currentY = plotArea.getPlotBottom() - (i + 1) * YSteps;
			// 横的grid线
			plotGrid.renderGridLinesHorizontal(plotArea.getPlotLeft(),
					currentY, plotArea.getPlotRight(), currentY);
			// 标签
			this.labelsAxis.renderAxisHorizontalTick(plotArea.getPlotLeft(),
					currentY, labelsAxis.getDataSet().get(i));
		}
	}

	/**
	 * 绘制横向柱形图
	 */
	protected void renderHorizontalBar() {
		//坐标系
		renderHorizontalBarDataAxis();
		renderHorizontalBarLabelAxis();

		// 得到Y 轴标签横向间距高度
		float YSteps = getHorizontalYSteps();

		// 画柱形
		// 依柱形宽度，多柱形间的偏移值 与当前数据集的总数据个数得到当前标签柱形要占的高度
		int barNumber = mDataSet.size();
		int currNumber = 0;
		List<Integer> ret = mFlatBar.getBarHeightAndMargin(YSteps, barNumber);
		int barHeight = ret.get(0);
		int barInnerMargin = ret.get(1);
		int labelBarUseHeight = barNumber * barHeight + (barNumber - 1)
				* barInnerMargin;

		float scrWidth = getAxisScreenWidth();
		float valueWidth = (float) dataAxis.getAxisRange();

		for (int i = 0; i < barNumber; i++) {
			// 得到标签对应的值数据集
			BarData bd = mDataSet.get(i);
			List<Double> barValues = bd.getDataSet();
			// 设置成对应的颜色
			mFlatBar.getBarPaint().setColor(bd.getColor());

			// 画同标签下的所有柱形
			int k = 1;
			for (Double bv : barValues) {
				float currLableY = plotArea.getPlotBottom() - (k) * YSteps;
				float drawBarButtomY = currLableY + labelBarUseHeight / 2;
				drawBarButtomY = drawBarButtomY - (barHeight + barInnerMargin)
						* currNumber;
				float drawBarTopY = drawBarButtomY - barHeight;

				// 宽度
				float valuePostion = (float) Math.round(scrWidth
						* ((bv - dataAxis.getAxisMin()) / valueWidth));

				// 画出柱形
				mFlatBar.renderBar(plotArea.getPlotLeft(), drawBarButtomY,
						plotArea.getPlotLeft() + valuePostion, drawBarTopY,
						this.mCanvas);

				// 柱形顶端标识
				mFlatBar.renderBarItemLabel(getFormatterItemLabel(bv),
						plotArea.getPlotLeft() + valuePostion,
						(float) Math.round(drawBarButtomY - barHeight / 2),
						mCanvas);

				k++;
			}
			currNumber++;
		}

		// Y轴线
		dataAxis.renderAxis(plotArea.getPlotLeft(), plotArea.getPlotBottom(),
				plotArea.getPlotLeft(), plotArea.getPlotTop());

		// X轴 线
		labelsAxis.renderAxis(plotArea.getPlotLeft(), plotArea.getPlotBottom(),
				plotArea.getPlotRight(), plotArea.getPlotBottom());
		// 画Key说明
		drawDataSetKey();
	}

	/**
	 * 绘制竖向柱形图
	 */
	protected void renderVerticalBar() {
		renderVerticalBarDataAxis();
		renderVerticalBarLabelsAxis();

		float axisScreenHeight = getAxisScreenHeight();
		float axisDataHeight = (float) dataAxis.getAxisRange();

		// 得到标签轴数据集
		List<String> dataSet = labelsAxis.getDataSet();
		float XSteps = getVerticalXSteps(dataSet.size() + 1);

		int barNumber = mDataSet.size();
		int currNumber = 0;
		List<Integer> ret = mFlatBar.getBarWidthAndMargin(XSteps, barNumber);
		int barWidth = ret.get(0);
		int barInnerMargin = ret.get(1);
		int labelBarUseWidth = barNumber * barWidth + (barNumber - 1)
				* barInnerMargin;

		// X 轴 即标签轴
		for (int i = 0; i < mDataSet.size(); i++) {
			// 得到标签对应的值数据集
			BarData bd = mDataSet.get(i);
			List<Double> barValues = bd.getDataSet();
			// 设成对应的颜色
			mFlatBar.getBarPaint().setColor(bd.getColor());

			// 画出标签对应的所有柱形
			for (int j = 0; j < barValues.size(); j++) {
				Double bv = barValues.get(j);

				float valuePostion = (float) Math.round(axisScreenHeight
						* ((bv - dataAxis.getAxisMin()) / axisDataHeight));

				float currLableX = plotArea.getPlotLeft() + (j + 1) * XSteps;
				float drawBarStartX = currLableX - labelBarUseWidth / 2;

				// 计算同标签多柱 形时，新柱形的起始X坐标
				drawBarStartX = drawBarStartX + (barWidth + barInnerMargin)
						* currNumber;
				// 计算同标签多柱 形时，新柱形的结束X坐标
				float drawBarEndX = drawBarStartX + barWidth;

				// 画出柱形
				mFlatBar.renderBar(drawBarStartX, plotArea.getPlotBottom(),
						drawBarEndX, plotArea.getPlotBottom() - valuePostion,
						this.mCanvas);

				// 在柱形的顶端显示上柱形的当前值
				mFlatBar.renderBarItemLabel(
						getFormatterItemLabel(bv),
						(float) Math.round(drawBarStartX + barWidth / 2),
						(float) Math.round(plotArea.getPlotBottom()
								- valuePostion), mCanvas);
			}
			currNumber++;
		}

		// 轴 线
		dataAxis.renderAxis(plotArea.getPlotLeft(), plotArea.getPlotBottom(),
				plotArea.getPlotRight(), plotArea.getPlotBottom());

		// 绘制标签各柱形集的说明描述
		drawDataSetKey();

	}

	public boolean render() throws Exception {
		// TODO Auto-generated method stub

		try {
			super.render();
			// 绘制图表
			switch (mDirection) {
				case HORIZONTAL: {
					renderHorizontalBar();
					break;
				}
				case VERTICAL: {
					renderVerticalBar();
					break;
				}
			}

		} catch (Exception e) {
			throw e;
		}
		return true;
	}

}
