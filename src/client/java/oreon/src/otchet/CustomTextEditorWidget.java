package oreon.src.otchet;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom multi-line text editor widget for reports
 */
public class CustomTextEditorWidget extends ClickableWidget {
    
    private final TextRenderer textRenderer;
    private final List<String> lines;
    private String placeholder = "";
    private int cursorLine = 0;
    private int cursorPos = 0;
    private int scrollOffset = 0;
    private boolean focused = false;
    private int maxLines;
    private int maxLineLength = 50;

    public CustomTextEditorWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
        this.textRenderer = textRenderer;
        this.lines = new ArrayList<>();
        this.lines.add(""); // Start with one empty line
        this.maxLines = height / 10; // Approximate lines that fit
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw background
        int color = focused ? 0xFFFFFFFF : 0xFFA0A0A0;
        context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xFF000000);
        context.drawBorder(getX(), getY(), getWidth(), getHeight(), color);

        // Draw text lines
        if (lines.isEmpty() || (lines.size() == 1 && lines.get(0).isEmpty())) {
            // Draw placeholder
            if (!placeholder.isEmpty() && !focused) {
                context.drawTextWithShadow(
                    textRenderer,
                    placeholder,
                    getX() + 4,
                    getY() + 4,
                    0x808080
                );
            }
        } else {
            // Draw actual text
            int visibleLines = Math.min(maxLines, lines.size() - scrollOffset);
            for (int i = 0; i < visibleLines; i++) {
                int lineIndex = i + scrollOffset;
                if (lineIndex < lines.size()) {
                    String line = lines.get(lineIndex);
                    context.drawTextWithShadow(
                        textRenderer,
                        line,
                        getX() + 4,
                        getY() + 4 + (i * 10),
                        0xFFFFFF
                    );
                }
            }
        }

        // Draw cursor if focused
        if (focused && cursorLine >= scrollOffset && cursorLine < scrollOffset + maxLines) {
            int displayLine = cursorLine - scrollOffset;
            String currentLineText = getCurrentLine();
            int cursorX = getX() + 4;
            if (cursorPos > 0 && cursorPos <= currentLineText.length()) {
                String beforeCursor = currentLineText.substring(0, cursorPos);
                cursorX += textRenderer.getWidth(beforeCursor);
            }
            int cursorY = getY() + 4 + (displayLine * 10);
            
            // Draw cursor line
            context.fill(cursorX, cursorY, cursorX + 1, cursorY + 8, 0xFFFFFFFF);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            setFocused(true);
            
            // Calculate cursor position based on click
            int clickY = (int) (mouseY - getY() - 4);
            int lineClicked = (clickY / 10) + scrollOffset;
            
            if (lineClicked >= 0 && lineClicked < lines.size()) {
                cursorLine = lineClicked;
                
                // Calculate cursor position in line
                int clickX = (int) (mouseX - getX() - 4);
                String line = lines.get(cursorLine);
                cursorPos = 0;
                
                for (int i = 0; i <= line.length(); i++) {
                    if (i == line.length()) {
                        cursorPos = i;
                        break;
                    }
                    
                    String substr = line.substring(0, i + 1);
                    int width = textRenderer.getWidth(substr);
                    if (width > clickX) {
                        cursorPos = i;
                        break;
                    }
                    cursorPos = i + 1;
                }
            }
            
            return true;
        } else {
            setFocused(false);
            return false;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!focused) return false;

        switch (keyCode) {
            case 257: // Enter
                insertNewLine();
                return true;
            case 259: // Backspace
                handleBackspace();
                return true;
            case 261: // Delete
                handleDelete();
                return true;
            case 262: // Right arrow
                moveCursorRight();
                return true;
            case 263: // Left arrow
                moveCursorLeft();
                return true;
            case 264: // Down arrow
                moveCursorDown();
                return true;
            case 265: // Up arrow
                moveCursorUp();
                return true;
        }
        
        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (!focused) return false;
        
        if (chr >= 32 && chr <= 126) { // Printable ASCII
            insertCharacter(chr);
            return true;
        }
        
        return false;
    }

    /**
     * Insert a character at cursor position
     */
    private void insertCharacter(char chr) {
        String currentLine = getCurrentLine();
        if (currentLine.length() < maxLineLength) {
            String newLine = currentLine.substring(0, cursorPos) + chr + currentLine.substring(cursorPos);
            lines.set(cursorLine, newLine);
            cursorPos++;
        }
    }

    /**
     * Insert a new line at cursor position
     */
    private void insertNewLine() {
        String currentLine = getCurrentLine();
        String beforeCursor = currentLine.substring(0, cursorPos);
        String afterCursor = currentLine.substring(cursorPos);
        
        lines.set(cursorLine, beforeCursor);
        lines.add(cursorLine + 1, afterCursor);
        
        cursorLine++;
        cursorPos = 0;
        
        // Ensure we don't exceed max lines
        while (lines.size() > 1000) { // Reasonable limit
            lines.remove(0);
            if (cursorLine > 0) cursorLine--;
        }
        
        adjustScroll();
    }

    /**
     * Handle backspace key
     */
    private void handleBackspace() {
        if (cursorPos > 0) {
            // Delete character before cursor
            String currentLine = getCurrentLine();
            String newLine = currentLine.substring(0, cursorPos - 1) + currentLine.substring(cursorPos);
            lines.set(cursorLine, newLine);
            cursorPos--;
        } else if (cursorLine > 0) {
            // Merge with previous line
            String currentLine = getCurrentLine();
            String previousLine = lines.get(cursorLine - 1);
            
            if (previousLine.length() + currentLine.length() <= maxLineLength) {
                cursorPos = previousLine.length();
                lines.set(cursorLine - 1, previousLine + currentLine);
                lines.remove(cursorLine);
                cursorLine--;
            }
        }
    }

    /**
     * Handle delete key
     */
    private void handleDelete() {
        String currentLine = getCurrentLine();
        if (cursorPos < currentLine.length()) {
            // Delete character after cursor
            String newLine = currentLine.substring(0, cursorPos) + currentLine.substring(cursorPos + 1);
            lines.set(cursorLine, newLine);
        } else if (cursorLine < lines.size() - 1) {
            // Merge with next line
            String nextLine = lines.get(cursorLine + 1);
            if (currentLine.length() + nextLine.length() <= maxLineLength) {
                lines.set(cursorLine, currentLine + nextLine);
                lines.remove(cursorLine + 1);
            }
        }
    }

    /**
     * Move cursor right
     */
    private void moveCursorRight() {
        String currentLine = getCurrentLine();
        if (cursorPos < currentLine.length()) {
            cursorPos++;
        } else if (cursorLine < lines.size() - 1) {
            cursorLine++;
            cursorPos = 0;
            adjustScroll();
        }
    }

    /**
     * Move cursor left
     */
    private void moveCursorLeft() {
        if (cursorPos > 0) {
            cursorPos--;
        } else if (cursorLine > 0) {
            cursorLine--;
            cursorPos = lines.get(cursorLine).length();
            adjustScroll();
        }
    }

    /**
     * Move cursor up
     */
    private void moveCursorUp() {
        if (cursorLine > 0) {
            cursorLine--;
            String newLine = lines.get(cursorLine);
            cursorPos = Math.min(cursorPos, newLine.length());
            adjustScroll();
        }
    }

    /**
     * Move cursor down
     */
    private void moveCursorDown() {
        if (cursorLine < lines.size() - 1) {
            cursorLine++;
            String newLine = lines.get(cursorLine);
            cursorPos = Math.min(cursorPos, newLine.length());
            adjustScroll();
        }
    }

    /**
     * Adjust scroll to keep cursor visible
     */
    private void adjustScroll() {
        if (cursorLine < scrollOffset) {
            scrollOffset = cursorLine;
        } else if (cursorLine >= scrollOffset + maxLines) {
            scrollOffset = cursorLine - maxLines + 1;
        }
    }

    /**
     * Get current line
     */
    private String getCurrentLine() {
        if (cursorLine >= 0 && cursorLine < lines.size()) {
            return lines.get(cursorLine);
        }
        return "";
    }

    /**
     * Set placeholder text
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    /**
     * Get all text as single string
     */
    public String getText() {
        return String.join("\n", lines);
    }

    /**
     * Set all text
     */
    public void setText(String text) {
        lines.clear();
        if (text.isEmpty()) {
            lines.add("");
        } else {
            String[] textLines = text.split("\n", -1);
            for (String line : textLines) {
                lines.add(line);
            }
        }
        cursorLine = 0;
        cursorPos = 0;
        scrollOffset = 0;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(net.minecraft.client.gui.screen.narration.NarrationPart.TITLE, getMessage());
    }
}